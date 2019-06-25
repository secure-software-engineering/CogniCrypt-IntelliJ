package de.fraunhofer.iem.icognicrypt.core.Xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;

public class XmlUtilities
{
    @Nullable
    public static Node FindFirstNode(File file, String expression) throws XPathExpressionException, FileNotFoundException
    {
        NodeList nodes = FindNodes(file, expression);
        if (nodes == null || nodes.getLength() == 0)
            return null;
        return nodes.item(0);
    }

    public static NodeList FindNodes(@NotNull File file, String expression) throws XPathExpressionException, FileNotFoundException
    {
        if (!file.exists())
            throw new FileNotFoundException();
        InputSource input = new InputSource(file.getAbsolutePath());
        return (NodeList) XPathFactory.newInstance().newXPath().evaluate(expression, input, XPathConstants.NODESET);
    }

    public static String GetAttributeValue(Node node, String attributeName){
        if (node == null || !node.hasAttributes())
            return null;
        Attr attribute =  (Attr) node.getAttributes().getNamedItem(attributeName);
        if (attribute == null)
            return null;
        return attribute.getValue();
    }
}
