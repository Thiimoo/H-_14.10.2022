/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria.webcrawler;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinkFinder implements Runnable {

    private String url;
    private ILinkHandler linkHandler;

    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, ILinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
        System.out.println("creating new linkfinder");
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        //if not already visited
        if (!linkHandler.visited(url)) {
            try {
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                List<String> urls = new ArrayList<String>();
                System.out.println("size of list: "+list.size());
                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    System.out.println("new extracted link");
                    if (!extracted.getLink().isEmpty()
                            && !linkHandler.visited(extracted.getLink())) {

                        urls.add(extracted.getLink());
                        System.out.println("added url: "+extracted.getLink());
                    }

                }
                linkHandler.addVisited(url);
                System.out.println("added url to visited links");
                if (linkHandler.size() == 500) {
                    System.out.println("Gebrauchte zeit für 500 Links = " + (System.nanoTime() - t0));
                }

                for (String l : urls) {
                    linkHandler.queueLink(l);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
