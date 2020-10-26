commit_hash=$(git rev-parse --short HEAD)
curl -X POST \
      -F messages=@infrastructure/target/cucumber_messages https://studio.cucumber.io/cucumber_project/results \
      -H "project-access-token: $CUCUMBER_PROJECT" \
      -H "provider: github" \
      -H "repo: danilat/scorecards" \
      -H "branch: master" \
      -H "revision: $commit_hash"