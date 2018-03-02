/*
 * Simple generator script to build GIS (GeoJSON) layer for maidenhead grid. Will generate 2, 4 or 6
 * character reference grids, however 6 character grid consists of a significant number of features
 * and thereby not really usable - indeed I've not been able to load it in QGIS, and it weighs in at
 * around 13GB of text.
 *
 * Nothing too special in the design, other than the reason it manually prints it out to stdout as
 * it runs rather than building a full map and then generating the JSON is due to the memory
 * requirements to support the 6 character reference grid. So, simple print as it builds and
 * designed to just be redirected to a file.
 *
 * So example run for 4 character grid reference:
 * $ groovy mhgen.groovy 4 > output.geojson
 */
 
import groovy.json.JsonBuilder

// We require one argument, and it must be 2, 4 or 6
if( args.size() != 1 || (args.size() == 1 && ['2', '4', '6'].contains( args[0] ) == false ) ) {
	println 'You must provide the level of grid squares you want.'
	println '2 character, 4 character or 6 character squares.'
	println 'ie. groovy mhgen.groovy 4'

	System.exit( 1 )
}

def mhg = new MaidenHeadGenerator()
int levels = Integer.parseInt( args[0] ) / 2
mhg.generate( levels )

// -- END --

//
// Main processing class
//
class MaidenHeadGenerator {
	private static final INCREMENTS = [
		[lonInc: 20,           latInc: 10,           refChar: 'A'..'R' ], // 2 character grid
		[lonInc: 2,            latInc: 1,            refChar: 0..9 ],     // 4 character grid
		[lonInc: 0.0833333333, latInc: 0.0416666667, refChar: 'a'..'x' ]  // 6 character grid
	]

	void generate( int levels ) {
		println '{ "type": "FeatureCollection", "features": ['
		buildGrid( levels, -180, -90 )
		println ']}'
	}

	// Build an array of GeoJSON features representing the Maidenhead Grid
	// Note: Recursive method
	void buildGrid(  int levels, def startLon, def startLat, int currentLevel = 1, String fieldPrefix = '' ) {
		// Read in the look up table values
		int incrementsIdx = currentLevel - 1
		def lonInc = INCREMENTS[incrementsIdx].lonInc,
		    latInc = INCREMENTS[incrementsIdx].latInc  // increments
		def curLon = startLon,
		    curLat = startLat
		def charRange = INCREMENTS[incrementsIdx].refChar

		for( latFieldCode in charRange ) {
			for( lonFieldCode in charRange ) {
				def fieldRef = "${fieldPrefix}${lonFieldCode}${latFieldCode}"

				if( currentLevel != levels ) {
					buildGrid( levels, curLon, curLat, currentLevel + 1, fieldRef )
				}
				else {
					def p1 = [curLon, curLat]
					def p2 = [curLon, curLat + latInc]
					def p3 = [curLon + lonInc, curLat + latInc]
					def p4 = [curLon + lonInc, curLat]

					println "${createFeature( fieldRef, [p1, p2, p3, p4, p1] )},"
				}

				curLon += lonInc
			}

			// Reset longitude vals
			curLon = startLon

			// Incrememnt latitude vals
			curLat += latInc
		}	
	}

	String createFeature( String squareRef, ArrayList points ) {
		// Create the GeoJSON feature
		def jb = new JsonBuilder()
		Map feature = jb {
			type 'Feature'
			geometry {
				type 'Polygon'
				coordinates( [points] )
			}
			properties {
				name squareRef
			}
		}

		jb.toString()
	}
}