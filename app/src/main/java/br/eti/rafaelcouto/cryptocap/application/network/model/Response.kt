package br.eti.rafaelcouto.cryptocap.application.network.model

class Response<T>(
    var data: T?,
    var status: Status,
    var error: String?
) {

    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    companion object {
        fun loading() = Response(null, Status.LOADING, null)
        fun <T> success(data: T) = Response(data, Status.SUCCESS, null)
        fun <T> error(error: String) = Response<T>(null, Status.ERROR, error)
    }
}
