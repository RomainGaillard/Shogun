package com.example.romainynov.shogun;

/**
 * Created by RomainYnov on 06/05/2015.
 */

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class DialogueXML {

    private ArrayList<String> dialog;
    private ArrayList<String> choices;

    //Constructeur paramétré
    public DialogueXML(String nameNpc,String condition, Context context)
    {
        InputStream is = context.getResources().openRawResource(R.raw.dialogshogun);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            XPathExpression expr = null;
            XPathExpression expr2 = null;

            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xpath = xFactory.newXPath();

            //on récupère tous les dialogues du npc "nameNpc"
            expr = xpath.compile("npcs/npc[@name='"+nameNpc+"']/dialog[@condition='"+condition+"']/text");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            //dialog = nodes.item(0).getTextContent();

            dialog = new ArrayList<String>();
            for(int i=0;i<nodes.getLength();i++)
            {
                dialog.add(nodes.item(i).getTextContent());
            }

            //on récupère tous les choix du npc "nameNpc"
            expr2 = xpath.compile("npcs/npc[@name='"+nameNpc+"']/dialog[@condition='"+condition+"']/choice");
            Object result2 = expr2.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes2 = (NodeList) result2;
            choices = new ArrayList<String>();
            for(int i=0;i<nodes2.getLength();i++)
            {
                choices.add(nodes2.item(i).getTextContent());
            }
        } catch (Exception e) { e.printStackTrace();}
    }

    public ArrayList<String> getDialog() {
        return dialog;
    }
    public ArrayList<String> getChoices() {
        return choices;
    }
}
