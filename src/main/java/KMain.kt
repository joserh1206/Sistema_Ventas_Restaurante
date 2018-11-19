import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class KMain : Application() {
    override fun start(primaryStage : Stage?) {
        var root : Parent = FXMLLoader.load(javaClass.classLoader.getResource("views/login.fxml"))
        var scene = Scene(root)
        if(primaryStage != null){
            primaryStage.scene = scene
            primaryStage.isResizable = false
            primaryStage.show()
        }
    }

    companion object {
        @JvmStatic
        fun main(args : Array<String>){
            launch(KMain::class.java, *args)
        }
    }
}