
package com.ava.ai.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Safe non-root placeholder.
 * Root operations are intentionally disabled.
 */
object RootShell {

    suspend fun isRootAvailable(): Boolean = false

    suspend fun execute(command: String): Result<String> =
        withContext(Dispatchers.IO) {
            Result.failure(
                UnsupportedOperationException("Root operations are disabled.")
            )
        }

    suspend fun execute(commands: List<String>): Result<List<String>> =
        withContext(Dispatchers.IO) {
            Result.failure(
                UnsupportedOperationException("Root operations are disabled.")
            )
        }

    suspend fun executeWithExitCode(command: String): Pair<Int, String> =
        Pair(-1, "Root operations are disabled.")

    object Operations {

        suspend fun rebootDevice() = execute("reboot")
        suspend fun rebootRecovery() = execute("reboot recovery")
        suspend fun rebootBootloader() = execute("reboot bootloader")
        suspend fun shutdownDevice() = execute("reboot -p")

        suspend fun killApp(packageName: String) = execute("am force-stop $packageName")
        suspend fun clearAppData(packageName: String) = execute("pm clear $packageName")
        suspend fun uninstallApp(packageName: String) = execute("pm uninstall $packageName")
        suspend fun installApk(apkPath: String) = execute("pm install -r $apkPath")

        suspend fun grantPermission(packageName: String, permission: String) =
            execute("pm grant $packageName $permission")

        suspend fun revokePermission(packageName: String, permission: String) =
            execute("pm revoke $packageName $permission")

        suspend fun setSystemProperty(key: String, value: String) =
            execute("setprop $key $value")

        suspend fun getSystemProperty(key: String) =
            execute("getprop $key")

        suspend fun mountSystemRW() = execute("mount -o rw,remount /system")
        suspend fun mountSystemRO() = execute("mount -o ro,remount /system")

        suspend fun deleteFile(path: String) = execute("rm -f $path")
        suspend fun deleteDirectory(path: String) = execute("rm -rf $path")

        suspend fun copyFile(source: String, destination: String) =
            execute("cp $source $destination")

        suspend fun moveFile(source: String, destination: String) =
            execute("mv $source $destination")

        suspend fun changePermissions(path: String, permissions: String) =
            execute("chmod $permissions $path")

        suspend fun changeOwner(path: String, owner: String) =
            execute("chown $owner $path")

        suspend fun createDirectory(path: String) =
            execute("mkdir -p $path")

        suspend fun listDirectory(path: String) =
            execute("ls -la $path")

        suspend fun readFile(path: String) =
            execute("cat $path")

        suspend fun writeFile(path: String, content: String) =
            execute("echo '$content' > $path")

        suspend fun appendToFile(path: String, content: String) =
            execute("echo '$content' >> $path")
    }
}
