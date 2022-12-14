/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashSet;

public class WebCrawler6 implements ILinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());
    //    private final Collection<String> visitedLinks = Collections.synchronizedList(new ArrayList<String>());
    private String url;
    private ExecutorService execService;

    public WebCrawler6(String startingURL, int maxThreads) {
        this.url = startingURL;
        execService = Executors.newFixedThreadPool(maxThreads);
        System.out.println("new webcrawler");
    }

    @Override
    public void queueLink(String link) throws Exception {
        startNewThread(link);
        System.out.println("querelink");
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public void addVisited(String s) {
        visitedLinks.add(s);
    }

    @Override
    public boolean visited(String s) {
        return visitedLinks.contains(s);
    }

    private void startNewThread(String link) throws Exception {
        System.out.println("starting new thread");
        execService.execute(new LinkFinder(link, this));
        System.out.println("thread executed");
    }

    private void startCrawling() throws Exception {
        startNewThread(this.url);
    }

    public static void main(String[] args) throws Exception {
        new WebCrawler6("http://www.javaworld.com", 64).startCrawling();
        System.out.println("end of main");
    }
}