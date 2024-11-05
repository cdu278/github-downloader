package cdu278.githubdownloader.core

sealed interface Result<out T, out Error> {

    class Ok<T>(val value: T) : Result<T, Nothing>

    class Failure<Error>(val error: Error) : Result<Nothing, Error>
}

inline fun <A, B, Error> Result<A, Error>.map(
    transform: (A) -> B,
): Result<B, Error> {
    return when (this) {
        is Result.Ok -> Result.Ok(transform(this.value))
        is Result.Failure -> this
    }
}