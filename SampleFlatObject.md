## Introduction ##

Horkizein library just defines the frameword we are going to use for reading and writing our Xml(s).
This document shows the UML class diagram for reading and writing a very simple flat object who contains nothing else than a bunch of fields (int, float, boolean and String).

## XML Reading ##

Reading is carried out by the Horkizein's XmlFiller class. XmlFiller takes advantage of Android's pulling parser,
from which it can read data, and pushes tag content to registered XmlPushables classes. XmlPushable subclasses then
have the task of correctly handle input data.

XmlPushable's interface is pretty straightforward:
  * `String` `getTag()`: returns the tag associated with the subclass. This string is used to univocally identify the object when XmlFiller parses the Xml.
  * `void` `pushStartTag(String tag)`: pushes the opening tag just pulled from the parser to the registered XmlPushable. Signals the start of a new tag.
  * `void` `pushAttribute(String tag, String name, String value)`: pushes tag's attributes to the registered XmlPushable.
  * `void` `pushText(String tag, String text)`: pushes tag's content to the registered XmlPushable.
  * `void` `pushEndTag(String tag)`: pushes the closing tag to the registered XmlPushable. Signals the end of a the current tag.

And here is the UML class diagram:<br><br>
<img src='http://wiki.horkizein.googlecode.com/git/img/flat_obj_mod.png' />

<h2>XML Writing</h2>
Writing is even simpler. The object just has to implement the XmlWritable interface. This interface contains just a method:<br>
<ul><li><code>void</code> <code>writeXml(XmlSerializer serializer)</code>: the object can use the serializer to add its tags and attributes.</li></ul>

And again here is the UML class diagram:<br><br>
<img src='http://wiki.horkizein.googlecode.com/git/img/flat_obj_wrt.png' />