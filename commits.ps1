$env:GIT_AUTHOR_DATE="2026-06-17T11:47:00+05:30"
$env:GIT_COMMITTER_DATE="2026-06-17T11:47:00+05:30"

git add .
git commit -m "Added Room database"

Remove-Item Env:GIT_AUTHOR_DATE
Remove-Item Env:GIT_COMMITTER_DATE

git push origin master