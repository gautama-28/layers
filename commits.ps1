$env:GIT_AUTHOR_DATE="2026-06-16T10:47:00+05:30"
$env:GIT_COMMITTER_DATE="2026-06-16T10:47:00+05:30"

git add .
git commit -m "Dummy data created"

Remove-Item Env:GIT_AUTHOR_DATE
Remove-Item Env:GIT_COMMITTER_DATE
