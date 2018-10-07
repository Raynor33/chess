package chess.utils

import java.net.ServerSocket

import com.github.simplyscala.MongoEmbedDatabase

trait FreePortFixture {

  protected def freePort = {
    var socket: ServerSocket = null
    try {
      socket = new ServerSocket(0)
      socket.getLocalPort
    }
    finally {
      socket.close()
    }
  }

  protected def withFreePortFixture(f: Int => Any) = {
    f(freePort)
  }
}
