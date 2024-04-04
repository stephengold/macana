package example;

import myworld.obsidian.ObsidianUI;
import myworld.obsidian.display.DisplayEngine;
import myworld.obsidian.display.GLSurfaceManager;
import myworld.obsidian.geometry.Dimension2D;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32C.*;

/**
 * Manages a dedicated OpenGL context ;&amp; rendering target for Obsidian
 *
 * TODO The Obsidian library should provide something equivalent to this class.
 */
public class ObsidianContext {

    protected final ObsidianUI ui;
    protected Dimension2D size;
    protected int msaa;

    protected long osr;
    protected int renderFBO;
    protected int renderColorBuf;
    protected int sampleFBO;
    protected int sampleTex;

    public ObsidianContext(ObsidianUI ui) {
        this.ui = ui;
    }

    public void init(Dimension2D size, int msaa, long windowHandle) {
        this.msaa = msaa;
        this.size = size;

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_STENCIL_BITS, 0);
        glfwWindowHint(GLFW_DEPTH_BITS, 0);

        // This is crucial - without this, antialiasing will be disabled on the framebuffer
        // and any Skia operations involving clipping will result in major flickering and image
        // corruption across the entire drawing canvas.
        glfwWindowHint(GLFW_SAMPLES, msaa);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        // Share OpenGL resources with the application window
        // We're not going to use this window's framebuffer, so make it small
        osr = glfwCreateWindow(1, 1, "Obsidian OSR", 0, windowHandle);
        createRenderSurface();
    }

    public void resize(Dimension2D size){
        int newHeight = (int) size.height();
        int newWidth = (int) size.width();
        ui.display().ifSet(d -> d.resize(newWidth, newHeight));

        this.size = size;
        createRenderSurface();
    }

    public int getTextureHandle(){
        return sampleTex;
    }

    public void render(){
        inContext(() -> {

            glBindFramebuffer(GL_FRAMEBUFFER, renderFBO);
            glClear(GL_COLOR_BUFFER_BIT);

            ui.render();

            glBindFramebuffer(GL_READ_FRAMEBUFFER, renderFBO);
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, sampleFBO);
            glBlitFramebuffer(
                    0, 0, getWidth(), getHeight(),
                    0, 0, getWidth(), getHeight(),
                    GL_COLOR_BUFFER_BIT,
                    GL_LINEAR);
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        });
    }

    public void close(){
        cleanRenderSurface();
        glfwDestroyWindow(osr);
    }

    protected void createRenderSurface(){
        inContext(() -> {

            cleanRenderSurface();

            // Create render buffer target
            renderColorBuf = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, renderColorBuf);
            glRenderbufferStorageMultisample(GL_RENDERBUFFER, msaa, GL_RGBA8, getWidth(), getHeight());
            glBindRenderbuffer(GL_RENDERBUFFER, 0);

            renderFBO = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, renderFBO);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, renderColorBuf);

            verifyFBO("OSR render buffer");

            glBindFramebuffer(GL_FRAMEBUFFER, 0);

            // Create sample buffer target
            sampleTex = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, sampleTex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_SRGB_ALPHA, getWidth(), getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
            /*
             * The initial setting of GL_TEXTURE_MIN_FILTER is
             * GL_NEAREST_MIPMAP_LINEAR, for which SPORT would require mipmaps.
             */
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);

            sampleFBO = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, sampleFBO);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, sampleTex, 0);

            verifyFBO("OSR sample buffer");

            glBindFramebuffer(GL_FRAMEBUFFER, 0);

            if(ui.getDisplay() != null){
                var surfaceManager = (GLSurfaceManager) ui.getDisplay().getSurfaceManager();

                surfaceManager.setSamples(msaa);
                surfaceManager.setFramebufferHandle(renderFBO);

                ui.getDisplay().resize(getWidth(), getHeight());

            }else{
                ui.setDisplay(DisplayEngine.createForGL(getWidth(), getHeight(), msaa, renderFBO));
            }

        });
    }

    protected void cleanRenderSurface(){
        inContext(() -> {
            if(renderColorBuf != 0){
                glDeleteRenderbuffers(renderColorBuf);
                renderColorBuf = 0;
            }

            if(renderFBO != 0){
                glDeleteFramebuffers(renderFBO);
                renderFBO = 0;
            }

            if(sampleTex != 0){
                glDeleteTextures(sampleTex);
                sampleTex = 0;
            }

            if(sampleFBO != 0){
                glDeleteFramebuffers(sampleFBO);
                sampleFBO = 0;
            }
        });

    }

    protected void verifyFBO(String bufferName){
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE){
            throw new IllegalStateException("Could not allocate %s framebuffer: 0x%x".formatted(bufferName, status));
        }
    }

    protected int getWidth(){
        return (int) size.width();
    }

    protected int getHeight(){
        return (int) size.height();
    }

    protected void inContext(Runnable r){
        var restore = glfwGetCurrentContext();
        if(restore != osr){
            glfwMakeContextCurrent(osr);
        }

        r.run();

        if(restore != osr){
            glfwMakeContextCurrent(restore);
        }
    }

}
