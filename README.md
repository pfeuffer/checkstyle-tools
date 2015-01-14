This repo contains small tools, to support your work with the results of
checkstyle in java. These tools will read the checkstyle result files.

1. CheckstyleSum
   This will sum up either the error count per java file or the overall count of
   each file. Doing so, you have a good overview what your errors are and where
   to start cleaning up.
2. CheckstyleSuppressions
   If you take over an existing project with lots of code and you want to
   introduce checkstyle, but the existing code has so much errors so that you
   cannot focus on you new code amidst all the existing warnings, this can
   help you. It will read the checkstyle results and create a suppression file,
   that will suppress all the existing errors. Thereafter you will find only
   new errors introduced due to changes.

# BUILD

Build this via `sbt assembly`.

# RUN

## Suppressions

You can create the suppressions by simply running

```
java -jar checkstyle-tools-assembly-0.1.1.jar
```

This will look for `checkstyle-result.xml` files starting at the current directory.

## Summary

To create a summary, use the following command:

```
java -cp checkstyle-tools-assembly-0.1.1.jar de.pfeufferweb.tools.checkstyle.CheckstyleSum <dir>
```

Mind that you have to specify the starting directory for this one.

# LICENSE

Copyright 2014 René Pfeuffer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

