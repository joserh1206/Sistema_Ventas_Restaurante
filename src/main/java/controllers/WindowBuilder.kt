package controllers

import javafx.event.ActionEvent
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.Parent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import java.io.IOException


class WindowBuilder {
    companion object {
        @Throws(IOException::class)
        fun createWindow(ruta: String, event: ActionEvent, tiulo: String) {
            val ventanaParent = FXMLLoader.load<Parent>(WindowBuilder::class.java.getResource(ruta))
            val ventanaScene = Scene(ventanaParent)
            val appStage = (event.source as Node).scene.window as Stage
            appStage.close()
            appStage.scene = ventanaScene
            appStage.title = tiulo
            appStage.isResizable = false
            appStage.show()
        }

        @Throws(IOException::class)
        fun createPopUp(ruta: String, event: ActionEvent, tiulo: String) {
            val ventanaParent = FXMLLoader(WindowBuilder::class.java.getResource(ruta))
            val root = ventanaParent.load<Parent>()
            val stage = Stage()
            stage.scene = Scene(root)
            stage.title = tiulo
            stage.isResizable = false
            stage.show()
        }

        fun closeWindow(event: ActionEvent) {
            val appStage = (event.source as Node).scene.window as Stage
            appStage.close()
        }
    }


}