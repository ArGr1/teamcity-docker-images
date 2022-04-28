package generated

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dockerCommand
import common.TeamCityDockerImagesRepo.TeamCityDockerImagesRepo

object push_hub_windows: BuildType(
{
name = "Push windows"
buildNumberPattern="%dockerImage.teamcity.buildNumber%-%build.counter%"
steps {
dockerCommand {
name = "pull teamcity-server%docker.buildImagePostfix%:2022.04-nanoserver-1809"
commandType = other {
subCommand = "pull"
commandArgs = "%docker.buildRepository%teamcity-server%docker.buildImagePostfix%:2022.04-nanoserver-1809"
}
}

dockerCommand {
name = "tag teamcity-server%docker.buildImagePostfix%:2022.04-nanoserver-1809"
commandType = other {
subCommand = "tag"
commandArgs = "%docker.buildRepository%teamcity-server%docker.buildImagePostfix%:2022.04-nanoserver-1809 %docker.deployRepository%teamcity-server:2022.04-nanoserver-1809"
}
}

dockerCommand {
name = "push teamcity-server%docker.buildImagePostfix%:2022.04-nanoserver-1809"
commandType = push {
namesAndTags = """
%docker.deployRepository%teamcity-server:2022.04-nanoserver-1809
""".trimIndent()
removeImageAfterPush = false
}
}

}
features {
freeDiskSpace {
requiredSpace = "3gb"
failBuild = true
}
dockerSupport {
cleanupPushedImages = true
loginToRegistry = on {
dockerRegistryId = "PROJECT_EXT_774"
}
}
swabra {
forceCleanCheckout = true
}
}
params {
param("system.teamcity.agent.ensure.free.space", "3gb")
}
requirements {
contains("docker.server.osType", "windows")
}
dependencies {
snapshot(PublishLocal.publish_local)
{
onDependencyFailure =  FailureAction.FAIL_TO_START
}
}
})

