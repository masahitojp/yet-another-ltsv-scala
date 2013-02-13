######################################
Yet another LTSV Manipulator for Scala
######################################

.. image:: https://secure.travis-ci.org/masahitojp/yet-another-ltsv-scala.png?branch=master

:Original: https://github.com/seratch/ltsv4s/

This library is inspired by

* https://metacpan.org/module/Text::LTSV.
* https://github.com/making/ltsv4j

What's LTSV
===========

http://ltsv.org/


How to try
==========

Easy try

::

    $ git clone git@github.com:masahitojp/yet-another-ltsv-scala.git ltsv
    $ cd ltsv
    $ sbt console

How to test
===========


To test it use SBT invoke: 'sbt test'

Incompatibilities with Original
===============================

- add wants/ignores method(inspired by Text::LTSV)

Prerequisites
=============

* JDK6+
* Scala 2.9.2 or  2.10.0

License
=======

Licensed under the Apache License, Version 2.0.