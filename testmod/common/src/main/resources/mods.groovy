ModsDotGroovy.make {
    def props = buildProperties

    license = props.MOD_LICENSE

    onForge {
        modLoader = 'javafml'
        loaderVersion = "[${props.MCFORGE_LOADER_VERSION},)"
    }

    onFabric {
        custom = [
                modmenu: [
                        links: [
                                'modmenu.discord': 'https://discord.apexstudios.dev'
                        ],
                        update_checker: false,
                        parent: props.MOD_ID
                ]
        ]
    }

    mod {
        modId = 'testmod'
        displayName = 'TestMod'
        version = this.version
        description = 'TestMod'

        onFabric {
            dependencies {
                minecraft {
                    versionRange = props.MINECRAFT_FABRIC_VERSION_RANGE
                }

                mod(props.MOD_ID) {
                    versionRange = ">=${this.version}"
                }
            }

            entrypoints {
                setMain('dev.apexstudios.testmod.fabric.entrypoint.FabricModInitializer')
            }
        }

        onForge {
            credits = 'ApexStudios'

            dependencies {
                minecraft {
                    versionRange = props.MINECRAFT_FORGE_VERSION_RANGE
                }

                forge {
                    versionRange = props.MCFORGE_VERSION_RANGE
                }

                mod(props.MOD_ID) {
                    versionRange = "[${this.version},)"
                }
            }
        }
    }
}