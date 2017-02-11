import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;

public class Test {
	
	public static void main(String[] args) {
		Config c = new Config();
		c.clearColor = new Color(0.2f, 0.5f, 0.9f);
		c.immediateMode = true;
		new Psilox(c).start();
	}
	
}
