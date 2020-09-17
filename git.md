# Basic Git Commands

## To Add A File to Git:

First, stage the files to be added. A `.` will stage everything in the current directory. Otherwise, you can replace it with the specific items you want to stage.  

`git add .` _OR_  
`git add index.html`

Once the file(s) have been added, you need to add a commit message for the changes you have made. You can also do this one of two ways:

`git commit -m "your commit message here"`  

I recommend doing it like this so that you don't have to deal with Vim, or whatever you set as your default text editor when you installed Git for Windows. Otherwise, you can also just do  

`git commit`  

and then type in your commit message into the text editor save, close, and continue on with  

`git push`  

to actually send your changes to Git. **You have to do commit to Git in this order.** It's easy to remember because it's alphabetical: add, commit, and then push. Always.

---

## To Retrieve Changes From Git:

Use Git to store the latest versions of your projects this semester. You can run all the above commands to send things to Git, and now to 'pull' everyone elses changes from the repository to your own machine, run:  

`git pull`

---

## To Figure Out What's New On Git (or on your machine):

This command will tell you whether you have changes on your machine that you haven't yet sent to Git or whether Git is ahead of what's on your computer.

`git status`

---

## Help! Git is aborting because I have changes that Git doesn't, and my stuff is junk anyway!

I don't recommend using this command as much as I do. Basically, if you don't care about any of the changes you have locally on your machine, and you want to force pull from Git, run this command and it'll trash anything you did and grab whatever is on Git.

`git reset --hard`
`git pull`

# Basic Terminal Commands

To list all the directories that are available from the directory you are currently in:  

`ls`

To move around to a different directory:

`cd directory_name`

To move backwards out of the directory you're currently in:  

`cd ..`

To create a new file in your current directory:

`touch file_name.extension`

To create a new directory in your current directory:

`mkdir directory_name`

To show the contents of a file in the command line:

`cat file_name.extension`

# Where do I even run any of these commands?

Depends. If you're on some kind of Linux system, Konsole. If you're on Mac, run it on Terminal. If you're on Windows, I would recommend downloading Git for Windows: https://git-scm.com/download/win

Everything is already set up for you if you're working off of Linux or Mac. Windows is a little tricker. Download Git for Windows at the above link using all the default options in the installer. Then, I would recommend running `cd` once to go move out one folder and then creating a git folder there by running `mkdir git`. Once you're there here are some other fun commands to run on Windows to get everything working:

1. Run the command `ssh-keygen -t rsa -C "your_email@example.com"` to generate a new SSH key. Use your own email in place, of course.
2. It's going to ask you to enter a file to save the key in. Just hit `Enter`.
3. It's gonna show you some random art it made. To copy the key itself into your clipboard, run the command `cat ~/.ssh/id_rsa.pub | clip`.
4. Now, go to `git.unl.edu` and sign in using Canvas.
5. Once you're signed in, click the button on the very top right of GitLab and go to `Settings`.
6. Go to `SSH Keys`, which is the 4th option from the bottom on the left-hand side of the screen.
7. In the `Key` box on the right, paste in the key that you copied from Git for Windows using that `cat ~/.ssh/id_rsa.pub | clip` command and assign it a name. 
8. Hit `Add Key`.
9. Congratulations. Assuming you entered in the key correctly, Git for Windows should now be set up on your computer. 

---

## I don't want to use the command line at all. What are my options?

GitKraken. SourceTree. GitHub Desktop. Sublime. Most modern IDEs have their own Git clients that you can figure out how to use. You could manually add your files to Git but that's going to be a terrible time. We'll update this section with setting these up (probably) later.

---

For the most part, this is probably the extent of the commands you'll be using on a day-to-day basis for this project. You're going to find way _way_ better guides out on the internet that'll let you do things like `git stash` and branching and all sorts of other fun stuff. But, this is probably good enough for now. 

**I strongly recommend having different group members work on different parts of the projects to avoid merge errors. Otherwise, if you start working on the same parts of the project at the same time, you're going to end up having lots of fun digging through a lot of jumbled garbage that trying to figure out what to keep and what to throw out. And Git will complain at you the entire time.**