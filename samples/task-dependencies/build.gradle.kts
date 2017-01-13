
tasks {

    val hello by new { // refactor friendly task definition
        doLast { println("Hello!") }
    }

    "goodbye" {
        dependsOn(hello)  // dependsOn task reference
        doLast { println("Goodbye!") }
    }

    "chat" {
        dependsOn("goodbye") // dependsOn task name
    }

    "mixItUp" {
        dependsOn(hello, "goodbye")
    }
}

defaultTasks("chat")
