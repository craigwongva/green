node {

  stage('Setup') {
    git([
      url: env.GIT_URL ? env.GIT_URL : 'http://github.com/craigwongva/green',
      branch: "master"
    ])
  }
}
