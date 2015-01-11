import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

def props = [:]
props.group = ask("Define value for 'group' [gex.example]: ", "gex.example", "group")
props.projectName = ask("Define value for 'projectName' [sample-project]: ", "sample-project", "projectName")
props.version = ask("Define value for 'version' [0.1.0-SNAPSHOT]: ", "0.1.0-SNAPSHOT", "version")
props.jdbcUrl = ask("Define value for 'jdbcUrl' [jdbc:postgresql://postgres:5432/rest_api]: ", "jdbc:postgresql://postgres:5432/rest_api", "jdbcUrl")
props.jdbcUsername = ask("Define value for 'jdbcUsername' [dbuser]: ", "dbuser", "jdbcUsername")
props.jdbcPassword = ask("Define value for 'jdbcPassword' [dbpass]: ", "dbpass", "jdbcPassword")

props.groupFolder = props.group.replace('.' as char, '/' as char)


processTemplates "**/application.yaml", props
processTemplates "gradle.properties", props
processTemplates "raml/package.json", props
processTemplates "raml/Gulpfile.js", props
processTemplates "raml/src/index.raml", props
processTemplates "*/src/main/groovy/**/*.groovy", props

def conversion = [
  'api/src/main/groovy/groupFolder/': FilenameUtils.concat('api/src/main/groovy/', props.groupFolder),
  'core/src/main/groovy/groupFolder/': FilenameUtils.concat('core/src/main/groovy/', props.groupFolder),
  'domain/src/main/groovy/groupFolder/': FilenameUtils.concat('domain/src/main/groovy/', props.groupFolder)
]

conversion.each { k, v ->
  File originApiFolder = new File(projectDir, k)
  File destFolder = new File(projectDir, v)
  destFolder.parentFile.mkdirs()

  FileUtils.moveDirectory(originApiFolder, destFolder)
}

