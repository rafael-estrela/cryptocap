package br.eti.rafaelcouto.cryptocap.application.network.model

data class Result<T>(
    var data: T?,
    var status: Status,
    var error: String?
) {

    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    companion object {
        fun <T> loading() = Result<T>(null, Status.LOADING, null)
        fun <T> success(data: T) = Result(data, Status.SUCCESS, null)
        fun <T> error(error: String) = Result<T>(null, Status.ERROR, error)
    }
}
