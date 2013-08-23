package com.bluemonkeydev.leagueofsteve.rankings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
//import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.bluemonkeydev.leagueofsteve.data.Person;

public class AutoPopulateRankings {

	/**
	 * @param args
	 * @throws ParserException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ParserException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		if (args.length < 2) {
			System.out.println("out of luck");
			return;
		}
		boolean rickRoll = false;
		if (args.length == 3) {
			if (args[2].equalsIgnoreCase("true")) {
				rickRoll = true;
			}
		}
		String rollUser = null;
		if (args.length == 4) {
			rollUser = args[2];
		}

        BufferedReader in = new BufferedReader(new FileReader(args[0]));

        StringBuffer input = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            input.append(line);
        }
		Parser parser = new Parser(input.toString());

		NodeFilter f = new OrFilter(
				new NodeFilter[] {
						new HasAttributeFilter("class","row1"),
						new HasAttributeFilter("class","row2"),
						new HasAttributeFilter("class","bgFan")
				});

		List<Person> people = new ArrayList<Person>();

        NodeList nodeList = parser.parse(f);
        NodeIterator nodeIterator = nodeList.elements();
        while (nodeIterator.hasMoreNodes()) {

        	// rank, name, score, correct, best score, best correct, champion
        	// <tr class="row1" align="right" valign="top"><td align="center">-</td><td align="left">Alex M (1)</td><td align="center">-</td><td align="center">-</td><td align="center">-</td><td align="center">-</td><td align="left">***********</td></tr>

            NodeFilter itemFilter = new TagNameFilter("td");

            String itemHtml = nodeIterator.nextNode().getChildren().toHtml();
            Parser item = new Parser(itemHtml);
            NodeList itemList = item.parse(itemFilter);
            NodeIterator itemIterator = itemList.elements();
            int counter = 0;
            Person person = new Person();
            while (itemIterator.hasMoreNodes()) {
            	Node blockNode = itemIterator.nextNode();
            	if (blockNode instanceof TableColumn) {
            		TableColumn subTag = (TableColumn) blockNode;
            		switch (counter) {
            		case 0: // rank
            			person.rank = subTag.getChildrenHTML();
            			break;
            		case 1:
// NOTE: During the pool, there are LINKS here which can be extracted!
//            			NodeFilter nameFilter = new TagNameFilter("a");
//                        Parser nameItem = new Parser(subTag.getChildrenHTML());
//                        NodeList nameNodeList = nameItem.parse(nameFilter);
//                        LinkTag linkTag = (LinkTag) nameNodeList.elementAt(0);
//            			person.name = linkTag.getChildrenHTML();
//            			person.url = linkTag.getLink();

            			person.name = subTag.getChildrenHTML();
            			break;
            		case 2:
            			person.score = subTag.getChildrenHTML();
            			break;
            		case 3:
            			person.correct = subTag.getChildrenHTML();
            			break;
            		case 4:
            			person.bestScore = subTag.getChildrenHTML();
            			break;
            		case 5:
            			person.bestCorrect = subTag.getChildrenHTML();
            			break;
            		case 6:
            			person.winner = subTag.getChildrenHTML();
            			break;
            		}
            		counter++;
            	}
            }
            if (!person.rank.startsWith("&nbsp;")) {
            	people.add(person);
            }
            System.out.println(person.rank + ":" + person.name + ":" + person.score + ":" + person.correct + ":" + person.bestScore + ":" + person.bestCorrect + ":" + person.winner);
        }

        in.close();

    	// Write to XML
        BufferedWriter out = new BufferedWriter(new FileWriter(args[1]));

        out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");out.newLine();
        out.write("<people>");out.newLine();
        for (Person person : people) {
	        out.write("    <person>");out.newLine();
	        if (rickRoll &&
	        		( rollUser != null && person.name.contains(rollUser))) {
	        	out.write("       <rickroll>true</rickroll>");out.newLine();
	        }
	        out.write("       <name>" + person.name + "</name>");out.newLine();
	        out.write("       <url>" + person.url + "</url>");out.newLine();
			out.write("       <rank>" + person.rank + "</rank>");out.newLine();
	        out.write("       <score>" + person.score + "</score>");out.newLine();
			out.write("       <correct>" + person.correct + "</correct>");out.newLine();
			out.write("       <bestscore>" + person.bestScore + "</bestscore>");out.newLine();
			out.write("       <bestcorrect>" + person.bestCorrect + "</bestcorrect>");out.newLine();
			out.write("       <winner>" + person.winner + "</winner>");out.newLine();
	        out.write("    </person>");out.newLine();
        }
        out.write("</people>");out.newLine();

        out.close();

	}

}
