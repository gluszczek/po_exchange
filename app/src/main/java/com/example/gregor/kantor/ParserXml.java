package com.example.gregor.kantor;

        import java.io.*;
        import java.net.URL;
        import java.util.ArrayList;

        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.ParserConfigurationException;

        import org.w3c.dom.Document;
        import org.w3c.dom.NodeList;
        import org.w3c.dom.Node;
        import org.w3c.dom.Element;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;

/**
 * Created by karol on 25.04.2017.
 */
public class ParserXml {

    ArrayList<Currency> currencyList = new ArrayList<>();


    String url = "http://www.nbp.pl/kursy/xml/LastA.xml";
    Document doc;

    public Currency find(String currencyName){
        for(Currency c: currencyList){
            if(c.getCurrencyName().equals(currencyName)){
                return c;
            }
        }
        return new Currency("null", "null", "null", "null");
    }

    public Document getDoc(){
        return doc;
    }

    public ArrayList<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(ArrayList<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public void parseXml(){
        (new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    URL urlConnection = new URL(url);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    doc = db.parse(new InputSource(urlConnection.openStream()));
                    doc.getDocumentElement().normalize();

                    NodeList nList = doc.getElementsByTagName("pozycja");

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;
                            Currency currency = new Currency(
                                    eElement.getElementsByTagName("nazwa_waluty").item(0).getTextContent(),
                                    eElement.getElementsByTagName("przelicznik").item(0).getTextContent(),
                                    eElement.getElementsByTagName("kod_waluty").item(0).getTextContent(),
                                    eElement.getElementsByTagName("kurs_sredni").item(0).getTextContent().replaceFirst(",", ".")
                            );
                            currencyList.add(currency);

                        }
                    }

                    URL urlConnection2 = new URL("http://www.nbp.pl/kursy/xml/LastC.xml");
                    DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db2 = dbf2.newDocumentBuilder();
                    Document doc2;
                    doc2 = db2.parse(new InputSource(urlConnection2.openStream()));
                    doc2.getDocumentElement().normalize();

                    NodeList nList2 = doc2.getElementsByTagName("pozycja");

                    for (int temp = 0; temp < nList2.getLength(); temp++) {

                        Node nNode = nList2.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;
                            for(int i = 0; i < currencyList.size();i++) {
                                if (eElement.getElementsByTagName("nazwa_waluty").item(0).getTextContent().equals(currencyList.get(i).getCurrencyName())) {
                                    String buy = eElement.getElementsByTagName("kurs_kupna").item(0).getTextContent().replaceFirst(",", ".");
                                    String sell = eElement.getElementsByTagName("kurs_sprzedazy").item(0).getTextContent().replaceFirst(",", ".");
                                    currencyList.get(i).setBuyRate(buy);
                                    currencyList.get(i).setSellRate(sell);
                                }

                            }
                        }
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        })).start();
    }



}