package model.score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * This class implements {@link Score}.
 * It's present a JsonScoreBuilder to build a JsonScore easier and without errors
 * <p>
 * Here is an example of how Builder can be used: 
 * <pre>{@code
 *     JsonScore score = new JsonScore.Builder()
 *                       	.nameFromString("MIKE")
 *                       	.score(150)
 *                       	.build();
 * }</pre>
 * 
 */
public class JsonScore implements Score<String, Integer> {

	private String name;
	private Integer score;
	
	/**
	 * @param name
	 *            The user name
	 * @param score
	 *            The score of the game
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public JsonScore(final String name, final Integer score) throws IOException {
	    this.name = name;
	    this.score = score;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getScore() {
		return this.score;
	}

	@Override
	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JsonScore other = (JsonScore) obj;
		return name.equals(other.name) 
		    && score.equals(other.score);	
	}
	
	public static class Builder {
		
		private String name;
		private Integer score;
		
		/**
		 * @param name
		 * 	          A string with the user name
		 * @return
		 *		  The {@link Builder} builder with user name
		 */
		public Builder nameFromString(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * @param path
		 * 	          A string with the path of the user name file
		 * @return The {@link Builder} builder with user name
		 * @throws IOException
		 * 		       if an I/O error occurs
		 */
		public Builder nameFromPath(String path) throws IOException {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			this.name = reader.readLine();
			reader.close();
			return this;
		}
		
		/**
		 * @param score
		 * 		    The integer score of the game
		 * @return The {@link Builder} builder with score
		 */
		public Builder score(Integer score) {
			this.score = score;
			return this;
		}
		
		/**
		 * 
		 * @return A {@link Score} only if there are not exceptions
		 * @throws IllegalStateException
		 *             if name or score are unset
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		public Score<String,Integer> build() throws IllegalStateException, IOException {
			if(this.name == null || this.score == null) {
				System.out.println(name);
				System.out.println(score);
				throw new IllegalStateException();
			}
			return new JsonScore(name, score);
		}
	}


}
