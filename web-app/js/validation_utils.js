function validateAccountNumber(nrb) {
    nrb = nrb.replace(/[^0-9]+/g, '');
    var Wagi = new Array(1, 10, 3, 30, 9, 90, 27, 76, 81, 34, 49, 5, 50, 15, 53, 45, 62, 38, 89, 17, 73, 51, 25, 56, 75, 71, 31, 19, 93, 57);
    if (nrb.length == 26) {
        nrb = nrb + "2521";
        nrb = nrb.substr(2) + nrb.substr(0, 2);
        var Z = 0;
        for (var i = 0; i < 30; i++) {
            Z += nrb[29 - i] * Wagi[i];
        }
        if (Z % 97 == 1) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}




