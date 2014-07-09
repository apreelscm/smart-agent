//= require PDocument
//= require PPage

function PdfPreviewManager() {
    this.scale = 1.5;
    this.currentDocument = 0;
    this.buffers = [];

    this.$currentPageEl = jQuery("#page_num");
    this.$totalPagesEl = jQuery("#page_count");
    this.$prevButtonEl = jQuery("#prevPdfPage");
    this.$nextButtonEl = jQuery("#nextPdfPage");
    this.$loadingBoxEl = jQuery("#pdfBox-content-loading");

    this.$pdfBoxEl = jQuery("#pdfPage");
    this.canvas = document.getElementById('pdfPage');
    this.globalContext = this.canvas.getContext('2d');

    var that = this;
    this.$prevButtonEl.on("click", function(e) {
        e.preventDefault();
        that.navigatePdfPageView("prev");
        return false;
    });

    this.$nextButtonEl.on("click", function(e) {
        e.preventDefault();
        that.navigatePdfPageView("next");
        return false;
    });
}

PdfPreviewManager.prototype.navigatePdfPageView = function (direction) {
    if (direction == "prev") {
        if (this.documents[this.currentDocument].currentPage <= 1)
            return;

        this.documents[this.currentDocument].currentPage--;

        if (this.documents[this.currentDocument].currentPage == 1) {
            this.$prevButtonEl.addClass("disabled");
        }
        else {
            this.$prevButtonEl.removeClass("disabled");
        }
    }
    else if (direction == "next") {
        this.$prevButtonEl.removeClass("disabled");

        if (this.documents[this.currentDocument].currentPage >= this.documents[this.currentDocument].pagesCount) {
            this.$nextButtonEl.addClass("disabled");
            return;
        }

        this.documents[this.currentDocument].currentPage++;

        if (this.documents[this.currentDocument].getCurrentPage().isReady == false) {
            this.$nextButtonEl.addClass("disabled");
        }

        if (this.documents[this.currentDocument].currentPage >= this.documents[this.currentDocument].pagesCount) {
            this.$nextButtonEl.addClass("disabled");
        }
    }

    this.$pdfBoxEl.css("display", "none");
    this.$loadingBoxEl.show();

    this.refreshPreview();
    this.$currentPageEl.html(this.documents[this.currentDocument].currentPage);
}

PdfPreviewManager.prototype.drawPage = function() {
    this.canvas.width = this.documents[this.currentDocument].getCurrentPage().viewport.width;
    this.canvas.height = this.documents[this.currentDocument].getCurrentPage().viewport.height;
    this.globalContext.clearRect( 0, 0, this.canvas.width, this.canvas.height );
    this.globalContext.drawImage(this.documents[this.currentDocument].getCurrentPage().getImage(), 0, 0);
}

PdfPreviewManager.prototype.refreshPreview = function() {
    if (this.documents[this.currentDocument].getCurrentPage() != undefined && this.documents[this.currentDocument].getCurrentPage().isReady == true) {
        this.$currentPageEl.html(this.documents[this.currentDocument].currentPage);
        this.$totalPagesEl.html(this.documents[this.currentDocument].pagesCount);
        this.$loadingBoxEl.hide();
        this.$pdfBoxEl.css("display", "block");
        if (this.documents[this.currentDocument].currentPage < this.documents[this.currentDocument].pagesCount) {
            this.$nextButtonEl.removeClass("disabled");
        }
        else {
            this.$nextButtonEl.addClass("disabled");
        }
        this.drawPage();
        return true;
    }
    return false;
}

PdfPreviewManager.prototype.setDocuments = function(documents) {
    this.documents = documents;
}

PdfPreviewManager.prototype.showDocument = function(id) {
    console.log("Showing document["+id+"]: " + this.documents[id].name);
    this.$loadingBoxEl.show();
    this.$pdfBoxEl.css("display", "none");
    this.currentDocument = id;

    for(var i = 0; i < this.documents.length; i++) {
        if(i != this.currentDocument) {
            this.documents[i].releaseAll();
        }
    }

    this.documents[this.currentDocument].currentPage = 1;

    for(var i = 0; i < this.buffers.length; i++) {
        delete this.buffers[i]
    }
    this.buffers = [];
    for(var i = 0; i < 12; i++) {
        var buff = {};

        buff.buff = document.createElement('canvas');
        buff.ctx = buff.buff.getContext('2d');
        this.buffers.push(buff);
    }

    this.documents[this.currentDocument].renderAll(this.buffers);
    var that = this;
    this.$prevButtonEl.addClass("disabled");
    var intervalId = setInterval(function() {
        var result = that.refreshPreview();
        if (result == true) {
            clearInterval(intervalId);
        }
    }, 1000);
}