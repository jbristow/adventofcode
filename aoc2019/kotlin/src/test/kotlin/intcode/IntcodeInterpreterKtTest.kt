package intcode

import arrow.core.right
import arrow.core.some
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class IntcodeInterpreterKtTest {

    @Test
    fun testAddFindInputsDefault() {
        val initialCode = mutableMapOf(
            0L to 3L,
            1L to 4L,
            2L to 1L,
            3L to 0L,
            4L to 1L,
            5L to 2L
        )
        val state = CurrentState(pointer = 2L.some())
        val code = initialCode.toMutableMap()
        handleCodePoint(code, state.right()).fold({
            fail<String>(it)
        }, { newState ->
            newState.pointer.fold({
                fail<String>("pointer should not be null")
            }, { newPointer ->
                assertThat(newPointer).isEqualTo(6L)
            })
            assertThat(code[2L]).isEqualTo(7)
        })
    }

    @Test
    fun testAddFindInputsAllRelative() {
        val initialCode = mutableMapOf(
            0L to 3L,
            1L to 4L,
            2L to 22201L,
            3L to 0L,
            4L to 1L,
            5L to 2L
        )
        val state = CurrentState(pointer = 2L.some(), relativeBase = 1L)
        val code = initialCode.toMutableMap()
        handleCodePoint(code, state.right()).fold({
            fail<String>(it)
        }, { newState ->
            newState.pointer.fold({
                fail<String>("pointer should not be null")
            }, { newPointer ->
                assertThat(newPointer).isEqualTo(6L)
            })
            assertThat(code[3L]).isEqualTo(22205L)
        })
    }

    @Test
    fun testAddFindInputsAllImmediate() {
        val initialCode = mutableMapOf(
            0L to 3L,
            1L to 4L,
            2L to 1101L,
            3L to 0L,
            4L to 1L,
            5L to 2L
        )
        val state = CurrentState(pointer = 2L.some(), relativeBase = 1L)
        val code = initialCode.toMutableMap()
        handleCodePoint(code, state.right()).fold({
            fail<String>(it)
        }, { newState ->
            newState.pointer.fold({
                fail<String>("pointer should not be null")
            }, { newPointer ->
                assertThat(newPointer).isEqualTo(6L)
            })
            assertThat(code[2L]).isEqualTo(1L)
        })
    }

    @Test
    fun testOffset() {
        val initialCode = mutableMapOf(
            0L to 109L,
            1L to 988L
        )
        val state = CurrentState()
        val code = initialCode.toMutableMap()
        handleCodePoint(code, state.right()).fold({
            fail<String>(it)
        }, { newState ->
            newState.pointer.fold({
                fail<String>("pointer should not be null")
            }, { newPointer ->
                assertThat(newPointer).isEqualTo(2L)
            })
            assertThat(code).isEqualTo(initialCode)
            println(newState)
            assertThat(newState.relativeBase).isEqualTo(988)
        })
    }
}
