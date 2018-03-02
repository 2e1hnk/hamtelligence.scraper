package scraper.dxcluster;

public class Spot {
	private String spotter;
	private Double frequency;
	private String dx;
	private String comments;
	private int time;
	private String gridsquare;

	public Spot() {

	}

	public Spot(String spotter, Double frequency, String dx, String comments, int time) {
		this.setSpotter(spotter);
		this.setFrequency(frequency);
		this.setDx(dx);
		this.setComments(comments.trim());
		this.setTime(time);
	}

	public Spot(String spotter, Double frequency, String dx, String comments, int time, String gridsquare) {
		this(spotter, frequency, dx, comments, time);
		this.setGridsquare(gridsquare);
	}

	/**
	 * @return the spotter
	 */
	public String getSpotter() {
		return spotter;
	}

	/**
	 * @param spotter
	 *            the spotter to set
	 */
	public void setSpotter(String spotter) {
		this.spotter = spotter;
	}

	/**
	 * @return the frequency
	 */
	public Double getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the dx
	 */
	public String getDx() {
		return dx;
	}

	/**
	 * @param dx
	 *            the dx to set
	 */
	public void setDx(String dx) {
		this.dx = dx;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the gridsquare
	 */
	public String getGridsquare() {
		return gridsquare;
	}

	/**
	 * @param gridsquare
	 *            the gridsquare to set
	 */
	public void setGridsquare(String gridsquare) {
		this.gridsquare = gridsquare;
	}

	public String toJson() {
		return String.format(
				"{\"spotter\": \"%s\", \"frequency\": %1.2f, \"dx\": \"%s\", \"comments\": \"%s\", \"time\": %s, \"gridsquare\": \"%s\"}",
				this.getSpotter(), this.getFrequency(), this.getDx(), this.getComments(), this.getTime(),
				this.getGridsquare());
	}

}
