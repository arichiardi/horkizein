# Changelog #
This Software use the following versioning schema:
  * 1st position - Version number
  * 2nd position - 0 for alpha, 1 for beta, 2 for release candidate, 3 for final release
  * 3rd position - revision / minor fixes number
  * suffix - the version status, the same as the second position, but just text.

## 0.0.7-alpha (new) ##
  * `[`FEATURE`]` Annotations are now necessary to build the desired graph of pulled out objects.
  * `[`FEATURE`]` The artifact is now on Maven Central.
  * Documentation on the new feature in progress. Please follow the examples in horkizein-it project for now.

## 0.0.2-alpha ##
  * `[`FEATURE`]` Added fillToken() to the XmlFiller class. This method read not only tags but also XML Metadata and fills the corresponding registered XmlPushable.
  * `[`FEATURE`]` Added a buffer to XmlFiller. Now the pushText() method is called just once per tag, regardless of how many TEXT events it pulled from the parser.
  * Improved all the tests, that are split in two parts now. The first set is executed using the standard Android parser, the second one uses a custom parser instead (KXmlParser mod) for having multiple TEXT events.

## 0.0.1-alpha ##
  * Project started.