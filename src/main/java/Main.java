import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "data.xml";

        List<Employee> xmlData = parseXML(fileName);

        String json = listToJson(xmlData);

        writeString("data2.json", json);
    }

    public static List<Employee> parseXML(String fileName) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Node root = null;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            root = doc.getDocumentElement();

            System.out.println("Корневой элемент: " + root.getNodeName());

        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return read(root);

    }

    private static List<Employee> read(Node node) {

        List<Employee> xmlData = new ArrayList<>();

// получаем список дочерних узлов текущего узла
        NodeList nodeList = node.getChildNodes();
// обходим список дочерних узлов
        int employeesCount = 0;
        Employee employee = new Employee();
        for (int i = 0; i < nodeList.getLength(); i++) {
// является ли текущий дочерний узел элементом?
            Node iNode = nodeList.item(i);
            if (Node.ELEMENT_NODE == iNode.getNodeType()) {

                // получаем список дочерних узлов элемента Employee
                if ("employee".equals(iNode.getNodeName())) {
                    NodeList emplList = iNode.getChildNodes();
                    System.out.println("Получили элемент employee:  ");
                    // обходим список дочерних узлов элемента Employee
                    for (int j = 0; j < emplList.getLength(); j++) {

                        // является ли текущий дочерний узел элементом?
                        Node jNode = emplList.item(j);
                        if (Node.ELEMENT_NODE == jNode.getNodeType()) {

                            System.out.println(jNode.getNodeName() + " = " + jNode.getTextContent());

                            // наполняем экземпляр Employee данными из XML
                            switch (jNode.getNodeName()) {
                                case "id" -> employee.id = (long) Float.parseFloat(jNode.getTextContent());
                                case "firstName" -> employee.firstName = jNode.getTextContent();
                                case "lastName" -> employee.lastName = jNode.getTextContent();
                                case "country" -> employee.country = jNode.getTextContent();
                                case "age" -> employee.age = Integer.parseInt(jNode.getTextContent());
                            }
                        }
                    }
                    // заносим данные в лист Employee
                    xmlData.add(employeesCount, employee);
                    employeesCount++;
                }
/*
// для доступа к атрибутам
                System.out.println("Значение" + iNode.getTextContent());
                Element element = (Element) iNode;

// получаем список атрибутов
                NamedNodeMap map = element.getAttributes();
                for (int a = 0; a < map.getLength(); a++) {
                    String attrName = map.item(a).getNodeName();
                    String attrValue = map.item(a).getNodeValue();
                    System.out.println("Атрибут: " + attrName + "; значение: " + attrValue);
                }
*/
                read(iNode);
            }
        }

        return xmlData;

    }


    public static String listToJson(List<Employee> list) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        return json;
    }

    public static void writeString(String fileName, String json) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}