/**
 * 
 */
package it.unipd.netmus.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author ValterTexasGroup
 *
 */
public final class PMF {
   
  private static final PersistenceManagerFactory pmfInstance =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  private PMF() {
  }

  public static PersistenceManagerFactory get() {
      return pmfInstance;
  }
}
