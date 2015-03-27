# Horkizein (not maintained anymore)

## Announcement
The library won't be updated anymore, unless specifically requested. Horkizen is a tiny tool I needed for some Android test and it's far from being complete at the moment. There are so many tools out there that can better handle Xml (or other data representation) than an !XmlPullParser wrapped in an !XmlPushParser :) 

## Overview
The word _horkizein_ is part of the well-known English verb "exorcism" (Greek: _exorkizein_ or _ex-horkizein_, where the ex- prefix means "out") which, in turn, derives from the Greek word _horkos_ whose significance is "oath".
It conveys the meaning of "to bind by an oath", "to put someone on oath" and this is what this tiny set of classes aims to to do.

We "horkizein" our Java classes in two ways:
 * *Design* *binding* both recently developed OO design best practices and Design Pattern principles recommend an extensive usage of composition and interfaces (over abstract classes and inheritance) to improve code resilience, flexibility and maintainability. Someone says that a good design lays out a contract between classes, including conditions and obligations, but I prefer to romantically picture it as a collaboration bond. The Horkizein Open Source Library defines the bond a Java object needs to cooperate with the Android !XmlPullParser interface.
 * *Xml* *Data* *Binding* this name defines the huge amount of tools modern software use for either materializing Xml files into computer memory (de-serialize) or generating Xml documents from objects (serialize). There is no romanticism this time, the Horkizein Open Source Library `*`is`*` an Xml Data Binding library.

To use Horkizein you need to download the jar files, branch the source or add the library using the following Maven dependency in your pom.xml:
{{{
    <dependency>
        <groupId>com.googlecode.horkizein</groupId>
        <artifactId>horkizein</artifactId>
        <version>${horkizein.version}</version>
    </dependency>
}}}

What you will get:
 * A very simple mechanism to bind your objects (just interfaces and annotations, custom compiler coming).
 * Maven artifact synced every release.

What you won't find:
 * Prefix/namespace support (it might work, it has not been tested yet).
 * Validation against an Xml schema.
 
## News
---
0.0.8-rc1 out!
---
 * horkizein [0.0.8-rc1] The software is now more DAO oriented. You can build (immutable) value objects from !XmlPushable after parsing or directly from the main class.
 * Cleaning of names of classes and methods. The naming should not change from now on.
 * Added examples to the documentation (especially of the !XmlTag annotation).
---
 * horkizein [0.0.7-alpha]: includes mayor changes. Annotations are now necesssary to build the desired graph of pulled out objects.
 * The artifact is now on Maven Central.
 * Documentation on the new feature in progress. Please follow the examples in horkizein-it project for now.

### Notes and widgets
The logo is the result of two-hour brain busting with metal puzzles. Thanks to [https://plus.google.com/110366719421604144194/posts Artem Kolyuka] for the contribution. Horkizein is also hosted at [http://www.ohloh.net/ Ohloh]. Click on the Ohloh widget below if your Android application is using it or give it a plus if you like it.
Thank you very much.

-ar
