package test.timely.search;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SearchTestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(LoginRyan.class);
    suite.addTestSuite(Search.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
