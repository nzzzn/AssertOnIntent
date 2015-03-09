This is a simple example of how to fuzz some Android IPC mechanisms. I find 
that a lot of code that handles Intents fails when null is specified for values 
they expect, this is usually with a crash. More sophisticated, domain specific 
fuzzing is of course required for applications that process information 
provided in intents. It isn't practical to fuzz them with random data usually, 
as the space of name value pairs in the extras is infinite, and memory is very 
finite. Programs written in the language Java tend to lack the exciting bugs we 
find in native code when handling strings or arrays and so are a little 
trickier in such constraints. Usually such bugs have to do with how a file name 
is generated base on input, where something gets loaded from, or application 
domain specific logic. For fuzzing such intents you could certainly take a 
fuzzer like this and drop in some code, or write a very simple program to fuzz 
it for you. I find writing a simple fuzzer is often best, and a side benefit is 
that they are easy to add to regression or smoke tests when your done.

I hope you find this tool useful.

Jesse Burns
iSEC Partners, Inc.
https://www.isecpartners.com

Android related questions reach me directly at: AndroidSecurityPaper@isecpartners.com