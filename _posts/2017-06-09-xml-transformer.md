---
title: Changed Formatting of XML Transformer
date: 2017-06-09
---


The behavior of [`javax.xml.transform.Transformer`](https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/Transformer.html) changed considerably.
Take the following sequence:

```java
// prepare an XML file as a DOM Source
String xml = "...";
Document document = DocumentBuilderFactory
		.newInstance()
		.newDocumentBuilder()
		.parse(new InputSource(new StringReader(xml)));
// maybe add some nodes or CDATA content

// transform the XML
Source source = new DOMSource(document);
Transformer transformer = TransformerFactory.newInstance().newTransformer();
// maybe configure transformer
StringWriter outputWriter = new StringWriter();
transformer.transform(source, new StreamResult(outputWriter));
String transformedXml = outputWriter.toString();

// now compare `xml` and `transformedXml`
```

Then the behavior between Java 8 and 9, with and without configuration, and with and without `xml:space="preserve"` differs.
This is the most common behavior:

* existing indentation remains
* new nodes, including CDATA are inline

It can be observed on Java 8 and 9 if the transformer was not configured beyond defaults and regardless of `xml:space="preserve"`.

But when you configure the transformer with `OutputKeys.INDENT=yes` and `indent-amount=4` Java 8 and 9 behave differently.
On Java 8 you get:

* existing indentation remains
* new nodes are indented according to the settings
* CDATA is not indented

On Java 9, though, this happens:

* entire document is indented according to settings, including a bunch of spurious empty lines
* accordingly new nodes are indented
* CDATA is put onto new lines and indented as well, fundamentally changing the XML!

Furthermore, with further configuration it becomes apparent that `xml:space="preserve"` behaves differently as well.
On Java 8 it has no effect, on Java 9 it drops us back into the "nothing changes" case described first.

[The test](src/test/java/wtf/java9/xml_transformer/TransformTest.java) demonstrates that behavior, using JUnit 5's cool nested tests. 

(Last checked: 8u131 and 9-ea+172-jigsaw)
