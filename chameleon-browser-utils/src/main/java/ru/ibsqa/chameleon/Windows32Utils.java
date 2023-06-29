package ru.ibsqa.chameleon;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility methods to handle windows outside the man program.
 */
public class Windows32Utils {

    private static User32 user32 = User32.INSTANCE;

    /**
     * Finds all visible window threads.
     *
     * @return All visible window threads.
     */
    public static  List<HWND> findAll() {
        final List<HWND> windows = new LinkedList<>();
        user32.EnumWindows(new User32.WNDENUMPROC() {
            @Override
            public  boolean callback(HWND hWnd, Pointer arg) {
                if (user32.IsWindowVisible(hWnd)) {
                    windows.add(hWnd);
                }
                return true;
            }
        }, null);
        return windows;
    }

    /**
     * Finds windows using the PID of their process among all visible windows.
     *
     * @param pid The PID of the process.
     * @return The windows whose process has the given PID.
     */
    public static  List<HWND> findByPID(long pid) {
        return findByPID(pid, findAll());
    }

    /**
     * Finds windows using the PID of their process.
     *
     * @param pid The PID of the process.
     * @param windows A list of windows in which the search is performed.
     * @return The windows whose process has the given PID.
     */
    public static  List<HWND> findByPID(long pid,
                                        List<HWND> windows) {

        final List<HWND> filteredWindows = new LinkedList<>();
        for (HWND window : windows) {
            IntByReference PIDRef = new IntByReference();
            user32.GetWindowThreadProcessId(window, PIDRef);
            if (PIDRef.getValue() == pid) {
                filteredWindows.add(window);
            }
        }
        return filteredWindows;
    }

    /**
     * Finds windows by their title among all visible windows.
     *
     * @param title A string contained in the title of the windows.
     * @return The windows having the given title.
     */
    public static  List<HWND> findByTitle(String title) {
        return findByTitle(title, findAll());
    }

    /**
     * Finds windows by their title.
     *
     * @param title A string contained in the title of the windows.
     * @param windows A list of windows in which the search is performed.
     * @return The windows having the given title.
     */
    public static  List<HWND> findByTitle(String title,
                                          List<HWND> windows) {

        final List<HWND> filteredWindows = new LinkedList<>();
        for (HWND window : windows) {
            char[] windowText = new char[512];
            user32.GetWindowText(window, windowText, 512);
            String currentTitle = Native.toString(windowText).trim();
            if (!currentTitle.isEmpty() && currentTitle.contains(title)) {
                filteredWindows.add(window);
            }
        }
        return filteredWindows;
    }

    /**
     * Sets the visibility of a window, including its icon in the taskbar.
     *
     * @param window The window.
     * @param visible True to show the window, false to hide it.
     */
    public static  void setVisible(HWND window, boolean visible) {
        user32.ShowWindow(window, visible ? 1 : 0);
    }

    public static void showWindow(long pid){
        List<HWND> windows = findByPID(pid);
        if (windows.size() == 0) {
            return;
        }
        HWND hwnd = windows.get(0);
        setVisible(hwnd, true);
        int foreThread  = user32.GetWindowThreadProcessId(user32.GetForegroundWindow(),  new IntByReference(0));
        long appThread   = Kernel32.INSTANCE.GetCurrentThreadId();

        int targetThread = user32.GetWindowThreadProcessId(hwnd, new IntByReference(0));

        user32.AttachThreadInput(new WinDef.DWORD(appThread), new WinDef.DWORD(foreThread), true);
        user32.AttachThreadInput(new WinDef.DWORD(targetThread), new WinDef.DWORD(appThread), true);

        user32.SetForegroundWindow(hwnd);

        user32.AttachThreadInput(new WinDef.DWORD(appThread), new WinDef.DWORD(foreThread), false);
        user32.AttachThreadInput(new WinDef.DWORD(targetThread), new WinDef.DWORD(appThread), false);
    }
}