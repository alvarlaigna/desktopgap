package org.smartly.application.desktopgap.impl.app.applications.compilers;

import org.smartly.Smartly;
import org.smartly.application.desktopgap.impl.app.IDesktopConstants;
import org.smartly.application.desktopgap.impl.app.applications.compilers.exceptions.InvalidManifestException;
import org.smartly.application.desktopgap.impl.app.applications.window.AppManifest;
import org.smartly.application.desktopgap.impl.resources.window.DeployerFrame;
import org.smartly.application.desktopgap.impl.app.utils.Utils;
import org.smartly.commons.util.FileUtils;
import org.smartly.commons.util.PathUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

/**
 * Application compiler. Build package and make executable.
 */
public class AppCompiler {

    private static final String MANIFEST = IDesktopConstants.MANIFEST;
    private static final String RUN_PAGE = IDesktopConstants.RUN_PAGE;

    public AppCompiler() {

    }

    /**
     * Creates a .dga package.<br/>
     * The compiler creates also the window.html file to wrap main application.
     *
     * @param manifestPath Path of manifest
     * @param outputDir    Output directory. i.e. "/root"
     * @return Path of output package. i.e. "/root/mypackage.dga"
     */
    public String buildPackage(final String manifestPath, final String outputDir) throws Exception {
        //-- creates executable frame --//
        this.makeExecutable(manifestPath);

        return "";
    }

    public void makeExecutable(final String manifestPath) throws Exception {
        if (!PathUtils.exists(manifestPath)) {
            throw new FileNotFoundException(manifestPath);
        }

        final AppManifest manifest = new AppManifest(manifestPath);
        if (!manifest.isValid()) {
            throw new InvalidManifestException(manifestPath);
        }

        this.make(manifest);
    }

    // ------------------------------------------------------------------------
    //                      p r i v a t e
    // ------------------------------------------------------------------------

    private void make(final AppManifest manifest) throws IOException {
        final String install_dir = manifest.getInstallDir();
        final String index = manifest.getIndex();
        final String index_file = PathUtils.merge(install_dir, index);
        final String app_dir = PathUtils.getParent(index_file);
        final String run_file = PathUtils.concat(app_dir, RUN_PAGE);

        final String index_html = FileUtils.readFileToString(new File(index_file));
        final String run_html = DeployerFrame.wrap(index_html, app_dir);
        FileUtils.writeStringToFile(new File(run_file), run_html, Smartly.getCharset());
    }

    // ------------------------------------------------------------------------
    //                      S T A T I C
    // ------------------------------------------------------------------------

    private static AppCompiler __instance;

    private static AppCompiler getInstance() {
        if (null == __instance) {
            __instance = new AppCompiler();
        }
        return __instance;
    }

    public static void make(final String path) throws Exception {
        if (Utils.isAppFolder(path)) {
            make(PathUtils.concat(path, MANIFEST));
        } else if (PathUtils.getFilename(path).equalsIgnoreCase(MANIFEST)) {
            getInstance().makeExecutable(path);
        } else {
            // only directories admitted here
            if (PathUtils.isFile(path)) {
                throw new InvalidManifestException(path);
            }
            // app folder or master dir?
            final Set<String> appFolders = Utils.getAppDirectories(path);
            for (final String folder : appFolders) {
                make(folder);
            }
        }
    }

}