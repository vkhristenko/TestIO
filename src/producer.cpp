#include <iostream>

#include "TFile.h"
#include "TTree.h"

#define NUM 100
#define NUM_EVENTS 10000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    // initialize
    TFile *f = new TFile("test.root", "recreate");
    TTree *t = new TTree("TestIO", "TestIO");

    double darr[NUM][NUM];
    t->Branch("darr", &darr, "darr[100][100]/D");

    for (auto i=0; i<NUM_EVENTS; i++) {
        if (i % 100 == 0)
            std::cout << "Event = " << i << " is being filled" << std::endl;

        for (auto ii=0; ii<NUM; ii++)
            for (auto jj=0; jj<NUM; jj++)
                darr[ii][jj] = ii + jj;

        t->Fill();
    }

    // close/write
    f->Write();
    f->Close();

    std::cout << "Bye Bye World" << std::endl;
}
