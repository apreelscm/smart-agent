function PPage(scale, num, data) {
    this.num = num;
    this.scale = scale;
    this.page = data;
    this.viewport = data.getViewport(this.scale);
    this.buffer = null;
    this.ctx = null;
    this.isReady = false;
}

PPage.prototype.renderToBuffer = function(width, height) {
    var buffer = document.createElement('canvas');
    buffer.width = width;
    buffer.height = height;
    return buffer;
}

PPage.prototype.render = function(buffer) {
    console.log("Page " + this.num + " rendering...");

    this.buffer = buffer.buff;
    this.ctx = buffer.ctx;

    this.buffer.width = this.viewport.width;
    this.buffer.height = this.viewport.height;

    var that = this;
    var renderContext = {
        canvasContext: that.ctx,
        viewport: that.viewport
    };
    this.page.render(renderContext).then(function() {
        console.log("Page " + that.num + " rendering complete!");
        that.ctx = null;
        that.isReady = true;
    });
}

PPage.prototype.getImage = function() {
    return this.buffer;
}

PPage.prototype.release = function() {
    delete this.buffer;
    delete this.ctx;
    this.isReady = false;
}