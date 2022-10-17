package net.eaustria.webcrawler;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class LinkFinderAction extends RecursiveAction {

    private String url;
    private ILinkHandler cr;

    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, ILinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {
        if (!cr.visited(url)) {
            try {
                List<RecursiveAction> actions = new ArrayList<RecursiveAction>();
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                System.out.println("size of NodesList: "+list.size());
                //TODO: list befüllen weil des duats nu nd deswegn geht er nd ind fori eini
                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    System.out.println("extended link"+ extracted);
                    if (!extracted.extractLink().isEmpty()
                            && !cr.visited(extracted.extractLink())) {

                        actions.add(new LinkFinderAction(extracted.extractLink(), cr));
                        System.out.println("added new action");
                    }
                }
                cr.addVisited(url);
                System.out.println("visited: " + url);
                if (cr.size() == 500) {
                    System.out.println("Gebrauchte zeit für 500 Links = " + (System.nanoTime() - t0));
                }

                invokeAll(actions);
            } catch (Exception e) {
            }
        }
    }
}

