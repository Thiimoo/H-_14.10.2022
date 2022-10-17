/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// This is the Interface which LinkFinder (for Java6) and LinkFinderAction (for
// Java7) will implement!

package net.eaustria.webcrawler;

public interface ILinkHandler {

    void queueLink(String link) throws Exception;

    int size();

    boolean visited(String link);

    void addVisited(String link);
}
