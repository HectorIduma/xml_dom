package xml_dom;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Xml_dom {

    public static void main(String[] args) {
        try {
            File archivo = new File("demo1.svg");
            DocumentBuilderFactory fabricaConstructorDocumentos = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructorDocumentos = fabricaConstructorDocumentos.newDocumentBuilder();
            Document documento = constructorDocumentos.parse(archivo);
            documento.getDocumentElement().normalize();

            Element elementoSvg = (Element) documento.getElementsByTagName("svg").item(0);
            double anchoSvg = Double.parseDouble(elementoSvg.getAttribute("width"));
            double altoSvg = Double.parseDouble(elementoSvg.getAttribute("height"));

            NodeList listaRectangulos = documento.getElementsByTagName("rect");

            for (int i = 0; i < listaRectangulos.getLength(); i++) {
                Element elementoRectangulo = (Element) listaRectangulos.item(i);

                String aAncho = elementoRectangulo.getAttribute("width");
                String aAlto = elementoRectangulo.getAttribute("height");
                
                double ancho = aAncho.endsWith("%") ? anchoSvg * (Double.parseDouble(aAncho.replace("%", "")) / 100) : Double.parseDouble(aAncho);
                double alto = aAlto.endsWith("%") ? altoSvg * (Double.parseDouble(aAlto.replace("%", "")) / 100) : Double.parseDouble(aAlto);

                double X = elementoRectangulo.hasAttribute("x") ? Double.parseDouble(elementoRectangulo.getAttribute("x")) : 0;
                double Y = elementoRectangulo.hasAttribute("y") ? Double.parseDouble(elementoRectangulo.getAttribute("y")) : 0;

                Element linea1 = documento.createElement("line");
                linea1.setAttribute("x1", String.valueOf(X));
                linea1.setAttribute("y1", String.valueOf(Y));
                linea1.setAttribute("x2", String.valueOf(X + ancho));
                linea1.setAttribute("y2", String.valueOf(Y));
                linea1.setAttribute("stroke", "black");
                linea1.setAttribute("stroke-width", "1");

                Element linea2 = documento.createElement("line");
                linea2.setAttribute("x1", String.valueOf(X + ancho));
                linea2.setAttribute("y1", String.valueOf(Y));
                linea2.setAttribute("x2", String.valueOf(X + ancho));
                linea2.setAttribute("y2", String.valueOf(Y + alto));
                linea2.setAttribute("stroke", "black");
                linea2.setAttribute("stroke-width", "1");

                Element linea3 = documento.createElement("line");
                linea3.setAttribute("x1", String.valueOf(X + ancho));
                linea3.setAttribute("y1", String.valueOf(Y + alto));
                linea3.setAttribute("x2", String.valueOf(X));
                linea3.setAttribute("y2", String.valueOf(Y + alto));
                linea3.setAttribute("stroke", "black");
                linea3.setAttribute("stroke-width", "1");

                Element linea4 = documento.createElement("line");
                linea4.setAttribute("x1", String.valueOf(X));
                linea4.setAttribute("y1", String.valueOf(Y + alto));
                linea4.setAttribute("x2", String.valueOf(X));
                linea4.setAttribute("y2", String.valueOf(Y));
                linea4.setAttribute("stroke", "black");
                linea4.setAttribute("stroke-width", "1");

                Node nodoPadre = elementoRectangulo.getParentNode();
                nodoPadre.insertBefore(linea1, elementoRectangulo);
                nodoPadre.insertBefore(linea2, elementoRectangulo);
                nodoPadre.insertBefore(linea3, elementoRectangulo);
                nodoPadre.insertBefore(linea4, elementoRectangulo);

                nodoPadre.removeChild(elementoRectangulo);
            }

            TransformerFactory fabricaTransformacion = TransformerFactory.newInstance();
            Transformer transformador = fabricaTransformacion.newTransformer();
            DOMSource fuente = new DOMSource(documento);
            StreamResult resultado = new StreamResult(new File("output.svg"));
            transformador.transform(fuente, resultado);

            System.out.println("Archivo guardado como output.svg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
