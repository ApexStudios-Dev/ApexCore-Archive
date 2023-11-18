ModsDotGroovy.make {
    def props = buildProperties

    license = props.MOD_LICENSE
    issueTrackerUrl = 'https://discord.apexstudios.dev/'
    // .toString() cause does not like GString
    // mixin = [ "${props.MOD_ID}-common.mixins.json".toString() ]

    onForge {
        modLoader = 'javafml'
        loaderVersion = "[${props['loom.platform'] == 'neoforge' ? props.NEOFORGE_LOADER_VERSION : props.MCFORGE_LOADER_VERSION},)"
    }

    onFabric {
        sourcesUrl = "https://github.com/ApexStudios-Dev/${props.MOD_NAME}"
        accessWidener = "${props.MOD_ID}.accesswidener"

        custom = [
                modmenu: [
                        links: [
                                'modmenu.discord': 'https://discord.apexstudios.dev'
                        ],
                        badges: [ 'library' ],
                        update_checker: true
                ]
        ]
    }

    mod {
        modId = props.MOD_ID
        displayName = props.MOD_NAME
        version = this.version
        updateJsonUrl = "https://api.modrinth.com/updates/${props.MOD_ID}/forge_updates.json"
        displayUrl = "https://apexstudios.dev/${props.MOD_ID}"
        description = props.MOD_DESCRIPTION

        contributor('Apex', 'Founder, Programmer')
        contributor('FantasyGaming', 'Co-Founder, Artist')
        contributor('RudySPG', 'Web-Developer, Supporter, Beta Tester')
        contributor('TobiSPG', 'Beta Tester')

        onFabric {
            logoFile = 'logo.png'

            author('ApexStudios')

            dependencies {
                minecraft {
                    versionRange = props.MINECRAFT_FABRIC_VERSION_RANGE
                }
            }

            entrypoints {
                setMain("${props.MOD_GROUP}.fabric.entrypoint.FabricModInitializer")
                // setClient("${props.MOD_GROUP}.fabric.entrypoint.${props.MOD_NAME}ClientModInitializer")
                // .toString() cause does not like GString
                // entrypoint('fabric-datagen', "${props.MOD_GROUP}.fabric.entrypoint.${props.MOD_NAME}ModInitializer".toString())
            }
        }

        onForge {
            credits = 'ApexStudios'
            displayTest = DisplayTest.MATCH_VERSION
            logoFile = 'banner.png'

            dependencies {
                minecraft {
                    versionRange = props.MINECRAFT_FORGE_VERSION_RANGE
                }

                forge {
                    if(props['loom.platform'] == 'neoforge') {
                        versionRange = props.NEOFORGE_VERSION_RANGE
                        modId = 'neoforge'
                    } else {
                        versionRange = props.MCFORGE_VERSION_RANGE
                    }
                }
            }
        }
    }
}