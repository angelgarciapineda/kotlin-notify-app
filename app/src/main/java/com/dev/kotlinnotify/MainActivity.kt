package com.dev.kotlinnotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //ID del canal
    private val channelID = "chaID"
    //Nombre del canal
    private  val channelName = "chaName"
    //ID  de la notificacion
    private  val notID = 0

    //El requestCode le ayuda a identificar de qué Intención regresó
    companion object {
        const val INTENT_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Se manda a llamar la función createNotificationChannel()
        createNotificationChannel()

        //Intent es un mecanismo de software que permite a los usuarios coordinar las funciones de diferentes actividades para lograr una tarea.
        val intent = Intent(this, Main2Activity::class.java)

        //PendingIntent es un símbolo que se le da a una aplicación extranjera
        // (por ejemplo, NotificationManager , AlarmManager , AlarmManager pantalla de AppWidgetManager u otras aplicaciones de terceros),
        // lo que permite a la aplicación extranjera utilizar los permisos de su aplicación para ejecutar un código predefinido .
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(INTENT_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        //El NotificationCompat.Builder es la forma más fácil de crear Notifications en todas las versiones de Android.
        // Incluso puedes usar funciones que están disponibles con Android 4.1. Si su aplicación se ejecuta en dispositivos con Android> = 4.1,
        // se utilizarán las nuevas características, si se ejecutan en Android <4.1, la notificación será una simple notificación antigua.
        val notification = NotificationCompat.Builder(this,channelID).also {
            //parametros de configuración de la notificación
            //Titulo de la notificición
            it.setContentTitle("Titulo de notificación")
            //Contenido
            it.setContentText("Soy una notificación")
            //Icono a mostrar de la notificacion
            it.setSmallIcon(R.drawable.ic_fire)
            //Prioridad de nuestra notificacion
            it.setPriority(NotificationCompat.PRIORITY_HIGH)
            //Se agrega el pendingintenta la notificacion
            it.setContentIntent(pendingIntent)
            //Destruir la notificación al tocarla
            it.setAutoCancel(true)
        }.build()

        val notificationManager = NotificationManagerCompat.from(this)

        button.setOnClickListener{
            notificationManager.notify(notID, notification)
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Se vuelve a indicar el nivel de importancia de la notificación
            val importance = NotificationManager.IMPORTANCE_HIGH


            val channel = NotificationChannel(channelID, channelName, importance).apply {
                //Parametros para modificar la notificación
                lightColor = Color.RED
                //Se habilitan las luces
                enableLights(true)
            }

            //getSystemService --==> Devuelva el identificador a un servicio de nivel de sistema por clase.
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }
}
