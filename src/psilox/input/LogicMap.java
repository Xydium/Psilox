package psilox.input;

import java.util.HashMap;
import java.util.Map;

public class LogicMap { 
	
	private Map<String, Question> map;
	private Map<String, Boolean> defaults;
	private Question evaluating;
	
	public LogicMap() {
		map = new HashMap<String, Question>();
		defaults = new HashMap<String, Boolean>();
	}
	
	public void addQuestion(String name, boolean intermediate, Question question) {
		map.putIfAbsent(name, question);
		defaults.putIfAbsent(name, intermediate);
	}
	
	public void removeQuestion(String name) {
		map.remove(name);
		defaults.remove(name);
	}
	
	public boolean ask(String name) {
		if(evaluating == null) {
			evaluating = map.get(name);
			boolean result = evaluating.evaluate(this);
			evaluating = null;
			return result;
		} else {
			Question e = map.get(name);
			if(evaluating == e) {
				return defaults.get(name);
			} else {
				return e.evaluate(this);
			}
		}
	}
	
	public static interface Question {
		public boolean evaluate(LogicMap l);
	}
	
}
