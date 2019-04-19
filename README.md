# junior_design
Project for Second Semester of Junior Design

Find Current Schedule/Backlog @ https://trello.com/b/74fKj9ei/team-8346

# Release Notes Version 1.0
Features:
    - In app User Login/Registration through both a personal account and Facebook
    - Ability to add move businesses to the database with a script
    - Allows users to see reviews they have left on any business within their profile page
    - Refined searching to include searching for subcategories in addition to business name
Bugs Fixes:
    - UI Fixes in the Main Page where the User selects business catagories
Known Bugs:
    - "See All" button provides multiple copies of the same business 

# Install Guide


# Git Command Tutorial:

Want to know what branch you are on currently?
    git branch
    -Will list out all the active branches and mark the one you are on with a star

Want to create a new branch?
    git checkout -b <branchname>
    -will create a new branch from last place you pulled

You have done some work and want to push it:
    git add .
    git commit -m "<insert message here>"
    git pull
    git push

    -if there were conflicts it will let you know at pull
    -if you want to pull those changes and merge manually:
        git stash
        git pull
        git merge stash

    -will automatically merge what it can and tell you what to manually merge

Have you done something terribly stupid and horrendous and want to go back without keeping changes?
    git reset --hard
    -yes those dashes are necessary
