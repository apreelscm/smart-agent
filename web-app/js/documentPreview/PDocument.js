function PDocument(num, name, scale, pdfData) {
    this.num = num;
    this.name = name;
    this.pdfData = pdfData;
    this.scale = scale;
    this.pdf = null;
    this.pages = [];
    this.pagesCount = 0;
    this.currentPage = 1;
    this.isReady = false;
}

PDocument.prototype.init = function() {
    var that = this;
    PDFJS.getDocument(this.pdfData).then(function(pdf) {
        that.pdf = pdf;
        that.currentPage = 1;
        that.pagesCount = pdf.numPages;
        that.isReady = true;
        that.preRender();
    });
}

PDocument.prototype.preRender = function() {
    console.log("Prerendering document["+this.num+"]: " + this.name + " in progress...");
    if (this.pages == undefined)
        this.pages = [];
    var that = this;
    for (var i = 1; i <= this.pagesCount; i++) {
        this.pdf.getPage(i).then(function(page) {
            var f = function(idx, p, t) {
                return function() { t.pages.push(new PPage(t.scale, idx, p)); };
            } (i, page, that);
            f();
        });
    }
}

PDocument.prototype.getCurrentPage = function() {
    return this.pages != undefined ? this.pages[this.currentPage-1] : undefined;
}

PDocument.prototype.renderAll = function(buffers) {
    this.init();
    var that = this;
    var intervalId = setInterval(function() {
        if (that.isReady == true) {
            for(var i = 0; i < that.pagesCount; i++)
                that.pages[i].render(buffers[i]);
            clearInterval(intervalId);
        }
    }, 1000);
}

PDocument.prototype.releaseAll = function() {
    delete this.pdf;
    if (this.pages != undefined) {
        for(var i = 0; i < this.pagesCount; i++) {
            this.pages[i].release();
        }
        delete this.pages;
    }
    this.isReady = false;
}