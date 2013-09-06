Tedd's Droid Tools
==================
Migrated from Google Code SVN repo to GitHub .git repo November '12.

My first published Android app which did two things:

  * answered the phone by pressing the camera button
  * put a screen guard up during an on-going call to prevent cheek hangups

The answer feature was nerfed by Google in Android 2.3.  The permission required to modify the phone state (i.e. answer an incoming call) was changed from anyone being able to use to only those applications installed in the original system ROM being able to use it.  In otherwords only manufacturers, carriers, Google or authors of ROM's for rooted phones could now use it.  Kinda sucked but was a fun project to work on.

6/14/2013 UPDATE: I did some reading around the other day and saw reports that the permission nerf only applied to the call that silences the ringer, not to the call that answers or ends the call.  I may one day pick this back up and try it out but if anyone tinkering with this code wants to give it a try I would be most interested in hearing about what you find.  Does it work?  Does the ringer stop when you answer an incoming call?

The screen guard feature is still helpful to some folks and I may fork the project off to a screen guard only app.

9/5/2013 UPDATE: sallespromanager was cool enough to test on Android 4.1.2 and found the internal services do not work. See https://github.com/teddscofield/teddsdroidtools/issues/2 Security exceptions are thrown.  Was fun while it lasted but that appears to be the end of the answer tools.


