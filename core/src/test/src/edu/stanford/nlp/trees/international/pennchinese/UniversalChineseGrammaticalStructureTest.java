// EnglishGrammaticalStructureTest -- unit tests for Stanford dependencies.
// Copyright (c) 2005, 2011, 2013 The Board of Trustees of
// The Leland Stanford Junior University. All Rights Reserved.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
// For more information, bug reports, fixes, contact:
//    Christopher Manning
//    Dept of Computer Science, Gates 1A
//    Stanford CA 94305-9010
//    USA
//    Support/Questions: parser-user@lists.stanford.edu
//    Licensing: parser-support@lists.stanford.edu

package edu.stanford.nlp.trees.international.pennchinese;

import java.util.*;

import edu.stanford.nlp.util.ErasureUtils;
import edu.stanford.nlp.util.Filters;
import junit.framework.TestCase;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.Pair;

/**
 * Test cases for conversion of Chinese TreeBank to Universal
 * Dependencies with Chinese characteristics.
 * This code is adapted from EnglishGrammaticalStructureTest.java
 *
 * @author Percy Liang
 * @author Peng Qi
 */
public class UniversalChineseGrammaticalStructureTest extends TestCase {

  // Return a string which is the concatenation of |items|, with a new line after each line.
  // "a", "b" => "a\nb\n"
  private static String C(String... items) {
    StringBuilder out = new StringBuilder();
    for (String x : items) {
      out.append(x);
      out.append('\n');
    }
    return out.toString();
  }

  // Create a new example
  private static Pair<String, String> T(String tree, String ans) {
    return new Pair<String, String>(tree, ans);
  }


  /**
   * Tests that we can extract the basic grammatical relations correctly from
   * some hard-coded trees.
   *
   * Sentence examples from the manual to at least test each relation.
   *
   */
  public void testBasicRelations() {
    Pair<String, String>[] examples = ErasureUtils.uncheckedCast(new Pair[] {
      // Gloss: Shanghai Pudong de orderly advance
      T("(NP (DNP (NP (NP (NR ??????)) (NP (NN ??????))) (DEG ???)) (ADJP (JJ ??????)) (NP (NN ??????)))",
        C("nmod:assmod(??????-2, ??????-1)", "nmod:assmod(??????-5, ??????-2)", "case(??????-2, ???-3)", "amod(??????-5, ??????-4)", "root(ROOT-0, ??????-5)")),

      // Gloss: Shanghai Pudong expansion and legal-system synchronizing
      T("(ROOT (IP (NP (NP (NR ??????) (NR ??????)) (NP (NN ??????) (CC ???) (NN ??????) (NN ??????))) (VP (VV ??????))))",
        C("name(??????-2, ??????-1)", "nmod:assmod(??????-6, ??????-2)", "conj(??????-6, ??????-3)", "cc(??????-6, ???-4)", "compound:nn(??????-6, ??????-5)", "nsubj(??????-7, ??????-6)", "root(ROOT-0, ??????-7)")),

      // Gloss: this-year
      T("(LCP (NP (NT ??????)) (LC ???))",
        C("root(ROOT-0, ??????-1)", "case(??????-1, ???-2)")),

      // Gloss: according country and Shanghai de relevant law
      T("(PP (P ??????) (NP (DNP (NP (NP (NN ??????)) (CC ???) (NP (NR ?????????))) (DEG ???)) (ADJP (JJ ??????)) (NP (NN ??????))))",
        C("case(??????-7, ??????-1)", "conj(?????????-4, ??????-2)", "cc(?????????-4, ???-3)", "nmod:assmod(??????-7, ?????????-4)", "case(?????????-4, ???-5)", "amod(??????-7, ??????-6)", "root(ROOT-0, ??????-7)")),

      // Gloss: building is expand Shanghai de primary economic activity
      T("(IP (NP (NN ??????)) (VP (VC ???) (NP (CP (IP (VP (VV ??????) (NP (NR ??????)))) (DEC ???)) (QP (CD ???) (CLP (M ???))) (ADJP (JJ ??????)) (NP (NN ??????) (NN ??????)))))",
        C("nsubj(??????-10, ??????-1)", "cop(??????-10, ???-2)", "acl(??????-10, ??????-3)", "dobj(??????-3, ??????-4)", "mark(??????-3, ???-5)", "nummod(??????-10, ???-6)", "mark:clf(???-6, ???-7)", "amod(??????-10, ??????-8)", "compound:nn(??????-10, ??????-9)", "root(ROOT-0, ??????-10)")),

      // Gloss: nickel has-been named modern industry de vitamins
      T("(IP (NP (NN ???)) (VP (SB ???) (VP (VV ??????) (NP (PU ???) (DNP (NP (ADJP (JJ ??????)) (NP (NN ??????))) (DEG ???)) (NP (NN ?????????)) (PU ???)))))",
        C("nsubjpass(??????-3, ???-1)", "auxpass(??????-3, ???-2)", "root(ROOT-0, ??????-3)", "punct(?????????-8, ???-4)", "amod(??????-6, ??????-5)", "nmod:assmod(?????????-8, ??????-6)", "case(??????-6, ???-7)", "dobj(??????-3, ?????????-8)", "punct(?????????-8, ???-9)")),

      // Gloss: once revealed then was included legal-system path
      T("(IP (VP (VP (ADVP (AD ???)) (VP (VV ??????))) (VP (ADVP (AD ???)) (VP (SB ???) (VP (VV ??????) (NP (NN ??????) (NN ??????)))))))))))",
        C("advmod(??????-2, ???-1)", "root(ROOT-0, ??????-2)", "advmod(??????-5, ???-3)", "auxpass(??????-5, ???-4)", "conj(??????-2, ??????-5)", // todo: was dep
                "compound:nn(??????-7, ??????-6)", "dobj(??????-5, ??????-7)")),

      T("(IP (NP (NP (NR ????????????)) (NP (NN ?????????)) (PRN (PU ???) (NP (NR ??????)) (PU ???)) (ADJP (JJ ??????)) (NP (NN ??????))) (VP (VC ???) (NP (CP (CP (IP (NP (NP (NR ????????????) (NN ??????) (NR ??????) (NN ??????)) (CC ???) (NP (NP (NR ??????) (NR ??????)) (NP (NN ?????????)) (ADJP (JJ ???)) (NP (NN ??????))) (CC ???) (NP (NP (NR ??????)) (NP (NR ??????)) (NP (NN ??????)) (ADJP (JJ ??????)) (NP (NN ??????)))) (VP (VV ??????))) (DEC ???))) (ADJP (JJ ??????)) (NP (NN ??????)))) (PU ???))",
        C("compound:nn(??????-7, ????????????-1)",
                "compound:nn(??????-7, ?????????-2)",
                "punct(??????-4, ???-3)",
                "parataxis:prnmod(??????-7, ??????-4)",
                "punct(??????-4, ???-5)",
                "amod(??????-7, ??????-6)",
                "nsubj(??????-28, ??????-7)",
                "cop(??????-28, ???-8)",
                "compound:nn(??????-12, ????????????-9)",
                "compound:nn(??????-12, ??????-10)",
                "compound:nn(??????-12, ??????-11)",
                "conj(??????-24, ??????-12)",
                "cc(??????-24, ???-13)",
                "name(??????-15, ??????-14)",
                "compound:nn(??????-18, ??????-15)",
                "compound:nn(??????-18, ?????????-16)",
                "amod(??????-18, ???-17)",
                "conj(??????-24, ??????-18)",
                "cc(??????-24, ???-19)",
                "compound:nn(??????-24, ??????-20)",
                "compound:nn(??????-24, ??????-21)",
                "compound:nn(??????-24, ??????-22)",
                "amod(??????-24, ??????-23)",
                "nsubj(??????-25, ??????-24)",
                "acl(??????-28, ??????-25)",
                "mark(??????-25, ???-26)",
                "amod(??????-28, ??????-27)",
                "root(ROOT-0, ??????-28)",
                "punct(??????-28, ???-29)")),

       T("(IP (NP (NR ??????) (NN ??????)) (VP (VV ??????) (NP (NN ??????) (NN ??????) (NN ??????)) (QP (CD ?????????) (CLP (M ???)))) (PU ???))",
               C("compound:nn(??????-2, ??????-1)",
                 "nsubj(??????-3, ??????-2)",
                 "root(ROOT-0, ??????-3)",
                 "compound:nn(??????-6, ??????-4)",
                 "compound:nn(??????-6, ??????-5)",
                 "dobj(??????-3, ??????-6)",
                 "nmod:range(??????-3, ?????????-7)",
                 "mark:clf(?????????-7, ???-8)",
                 "punct(??????-3, ???-9)")),

        T("(VP (NP (NT ??????)) (ADVP (AD ???)) (ADVP (AD ???)) (VP (VV ??????) (AS ???))))",
                    C("nmod:tmod(??????-4, ??????-1)",
                      "neg(??????-4, ???-2)",
                      "advmod(??????-4, ???-3)",
                      "root(ROOT-0, ??????-4)",
                      "aux:asp(??????-4, ???-5)")),

       // Test cases from CTB9, hand-annotated and might be unreliable (pengqi)
       T("( (IP (ADVP (AD ??????)) (NP (PN ??????)) (VP (ADVP (AD ???)) (VP (VV ???) (VP (VV ??????) (NP (DNP (NP (NR ????????????)) (NP (NN ??????) (NN ??????)) (DEG ???)) (NP (NN ??????)))))) (PU ???))) ",
               C("advmod(??????-5, ??????-1)",
                 "nsubj(??????-5, ??????-2)",
                 "advmod(??????-5, ???-3)",
                 "xcomp(??????-5, ???-4)",
                 "root(ROOT-0, ??????-5)",
                 "dep(??????-8, ????????????-6)",  // should be nmod:assmod or compound:nn
                 "compound:nn(??????-8, ??????-7)",
                 "nmod:assmod(??????-10, ??????-8)",
                 "case(??????-8, ???-9)",
                 "dobj(??????-5, ??????-10)",
                 "punct(??????-5, ???-11)")),

       // note: the original parentheses in this example don't seem to make sense...
       T("( (IP (ADVP (AD ???)) (NP (NR ??????) (NN ?????????)) (VP (NP (NT ??????)) (ADVP (AD ???)) (PP (P ???) (IP (NP (NR ??????) (NN ??????) (NN ??????)) (VP (VV ??????) (IP (NP (NR ??????)) (VP (ADVP (AD ???)) (ADVP (AD ??????)) (VP (VV ??????) (NP (NN ??????) (NN ??????)))))))) (VP (VV ??????) (IP (VP (VV ??????))))) (PU ???)))",
               C("advmod(??????-17, ???-1)",
                 "compound:nn(?????????-3, ??????-2)", // should be nmod:assmod?
                 "nsubj(??????-17, ?????????-3)",
                 "nmod:tmod(??????-17, ??????-4)",
                 "advmod(??????-17, ???-5)",
                 "case(??????-10, ???-6)",
                 "compound:nn(??????-9, ??????-7)",  // should be nmod:assmod?
                 "compound:nn(??????-9, ??????-8)",
                 "nsubj(??????-10, ??????-9)",
                 "nmod:prep(??????-17, ??????-10)",
                 "nsubj(??????-14, ??????-11)",
                 "advmod(??????-14, ???-12)",
                 "advmod(??????-14, ??????-13)",
                 "ccomp(??????-10, ??????-14)",
                 "compound:nn(??????-16, ??????-15)",
                 "dobj(??????-14, ??????-16)",
                 "root(ROOT-0, ??????-17)",
                 "ccomp(??????-17, ??????-18)",
                 "punct(??????-17, ???-19)")),

       T("( (IP (NP (NP (NP (NR ?????????)) (NP (NN ??????))) (NP (NR ???????????????))) (VP (NP (NT ??????)) (PP (P ??????) (NP (NP (NR ?????????)) (NP (NN ??????) (NN ??????)))) (VP (VSB (VV ??????) (VV ???)) (PU ???) (IP (PP (P ???) (LCP (NP (CP (CP (IP (VP (ADVP (AD ???)) (VP (VV ??????) (IP (NP (CP (CP (IP (VP (VA ??????))) (DEC ???))) (NP (NP (NP (NN ??????) (NN ??????)) (NP (NR ?????????))) (NP (NN ??????)))) (VP (VV ??????) (PP (P ???) (NP (NP (NP (NR ??????)) (NP (NN ??????))) (NP (NR ?????????))))))))) (DEC ???))) (NP (QP (CD ??????) (CLP (M ???))) (NP (NN ??????)))) (LC ??????))) (PU ???) (NP (NN ??????)) (VP (ADVP (AD ??????)) (VP (VRD (VV ??????) (VV ???)) (AS ???) (NP (QP (CD ??????) (CLP (M ???))) (NP (NP (NR ?????????)) (NP (NN ??????))))))))) (PU ???))) ",
               C("nmod:assmod(??????-2, ?????????-1)",
                 "appos(???????????????-3, ??????-2)",
                 "nsubj(???-10, ???????????????-3)",
                 "nmod:tmod(???-10, ??????-4)",
                 "case(??????-8, ??????-5)",
                 "nmod:assmod(??????-8, ?????????-6)",
                 "compound:nn(??????-8, ??????-7)",
                 "nmod:prep(???-10, ??????-8)",
                 "compound:vc(???-10, ??????-9)",
                 "root(ROOT-0, ???-10)",
                 "punct(???-10, ???-11)",
                 "case(??????-29, ???-12)",
                 "advmod(??????-14, ???-13)",
                 "acl(??????-29, ??????-14)",
                 "amod(??????-20, ??????-15)",
                 "mark(??????-15, ???-16)",
                 "compound:nn(??????-18, ??????-17)",
                 "appos(?????????-19, ??????-18)",
                 "compound:nn(??????-20, ?????????-19)",
                 "nsubj(??????-21, ??????-20)",
                 "ccomp(??????-14, ??????-21)",
                 "case(?????????-25, ???-22)",
                 "nmod:assmod(??????-24, ??????-23)",
                 "nmod(?????????-25, ??????-24)",
                 "nmod:prep(??????-21, ?????????-25)",
                 "mark(??????-14, ???-26)",
                 "nummod(??????-29, ??????-27)",
                 "mark:clf(??????-27, ???-28)",
                 "nmod:prep(??????-34, ??????-29)",
                 "case(??????-29, ??????-30)",
                 "punct(??????-34, ???-31)",
                 "nsubj(??????-34, ??????-32)",
                 "advmod(??????-34, ??????-33)",
                 "ccomp(???-10, ??????-34)",
                 "advmod:rcomp(??????-34, ???-35)",
                 "aux:asp(??????-34, ???-36)",
                 "nummod(??????-40, ??????-37)",
                 "mark:clf(??????-37, ???-38)",
                 "nmod:assmod(??????-40, ?????????-39)",
                 "dobj(??????-34, ??????-40)",
                 "punct(???-10, ???-41)")),
       T("( (IP (IP (NP (NR ???????????????)) (LCP (IP (VP (NP (NT ??????)) (VP (VV ??????) (IP (NP (NP (NP (NR ?????????)) (QP (CD ???) (CLP (M ???))) (NP (NN ??????)))) (VP (VV ??????)))))) (LC ???)) (VP (VV ???) (PU ???) (IP (NP (NP (NN ??????) (NN ????????????)) (NP (NR ?????????))) (VP (VP (VV ???) (NP (NN ??????))) (VP (PP (P ???) (NP (PN ???))) (VP (VSB (VV ??????) (VV ???)) (PU ???) (IP (NP (NP (NR ?????????)) (CC ???) (NP (PN ???) (NN ?????????))) (VP (ADVP (AD ??????)) (VP (VV ???) (NP (DNP (NP (NR ?????????) (NR ?????????)) (DEG ???)) (NP (NR ??????)))))))))))) (PU ???) (PU ???) (IP (VP (VV ??????) (VP (VC ???) (NP (CP (CP (IP (VP (VA ??????))) (DEC ???))) (NP (NN ??????) (NN ??????)))))) (PU ???))) ",
               C("nsubj(???-10, ???????????????-1)",
                 "nmod:tmod(??????-3, ??????-2)",
                 "advcl:loc(???-10, ??????-3)",
                 "nmod(??????-7, ?????????-4)",
                 "nummod(??????-7, ???-5)",
                 "mark:clf(???-5, ???-6)",
                 "nsubj(??????-8, ??????-7)",
                 "ccomp(??????-3, ??????-8)",
                 "case(??????-3, ???-9)",
                 "root(ROOT-0, ???-10)",
                 "punct(???-10, ???-11)",
                 "compound:nn(????????????-13, ??????-12)",
                 "appos(?????????-14, ????????????-13)",
                 "nsubj(???-15, ?????????-14)",
                 "ccomp(???-10, ???-15)",
                 "dobj(???-15, ??????-16)",
                 "case(???-18, ???-17)",
                 "nmod:prep(???-20, ???-18)",
                 "compound:vc(???-20, ??????-19)",
                 "conj(???-15, ???-20)",
                 "punct(???-20, ???-21)",
                 "conj(?????????-25, ?????????-22)",
                 "cc(?????????-25, ???-23)",
                 "nmod:poss(?????????-25, ???-24)",
                 "nsubj(???-27, ?????????-25)",
                 "advmod(???-27, ??????-26)",
                 "ccomp(???-20, ???-27)",
                 "name(?????????-29, ?????????-28)",
                 "nmod:assmod(??????-31, ?????????-29)",
                 "case(?????????-29, ???-30)",
                 "dobj(???-27, ??????-31)",
                 "punct(???-10, ???-32)",
                 "punct(???-10, ???-33)",
                 "xcomp(??????-39, ??????-34)",
                 "cop(??????-39, ???-35)",
                 "amod(??????-39, ??????-36)",
                 "mark(??????-36, ???-37)",
                 "compound:nn(??????-39, ??????-38)",
                 "conj(???-10, ??????-39)",
                 "punct(???-10, ???-40)")),

       T("( (IP (IP (NP (NN ??????) (NN ??????)) (VP (VP (VV ??????) (AS ???) (NP (DNP (NP (NR ????????????)) (DEG ???)) (NP (DP (DT ???) (CLP (M ???))) (ADJP (JJ ??????)) (NP (NN ??????) (CC ???) (NN ??????))))) (VP (VV ??????) (NP (NN ??????))))) (PU ???) (IP (ADVP (AD ???)) (NP (NP (NR ??????)) (NP (NN ??????) (NN ??????))) (VP (VC ???) (NP (CP (CP (IP (VP (VP (VV ??????)))) (DEC ???))) (NP (ADJP (JJ ??????)) (NP (NN ??????)))))) (PU ???))) ",
               C("compound:nn(??????-2, ??????-1)",
                 "nsubj(??????-3, ??????-2)",
                 "root(ROOT-0, ??????-3)",
                 "aux:asp(??????-3, ???-4)",
                 "nmod:assmod(??????-12, ????????????-5)",
                 "case(????????????-5, ???-6)",
                 "det(??????-12, ???-7)",
                 "mark:clf(???-7, ???-8)",
                 "amod(??????-12, ??????-9)",
                 "conj(??????-12, ??????-10)",
                 "cc(??????-12, ???-11)",
                 "dobj(??????-3, ??????-12)",
                 "conj(??????-3, ??????-13)",
                 "dobj(??????-13, ??????-14)",
                 "punct(??????-3, ???-15)",
                 "advmod(??????-24, ???-16)",
                 "nmod:assmod(??????-19, ??????-17)",
                 "compound:nn(??????-19, ??????-18)",
                 "nsubj(??????-24, ??????-19)",
                 "cop(??????-24, ???-20)",
                 "acl(??????-24, ??????-21)",
                 "mark(??????-21, ???-22)",
                 "amod(??????-24, ??????-23)",
                 "conj(??????-3, ??????-24)",
                 "punct(??????-3, ???-25)")),

       T("( (IP (IP (NP (DP (DT ??????)) (CP (CP (IP (VV ??????)) (DEC ???))) (NP (NN ??????))) (VP (VV ??????) (NP (CP (CP (IP (VP (VA ??????))) (DEC ???))) (NP (NN ??????))))) (PU ???) (IP (NP (PN ??????)) (VP (VP (VV ???) (AS ???) (NP (NN ??????))) (PU ???) (VP (VV ???) (AS ???) (NP (ADJP (JJ ???)) (NP (NN ???)))) (PU ???) (VP (VV ??????) (CP (IP (NP (NR ???????????????)) (VP (ADVP (AD ??????)) (VP (VV ??????)))) (SP ???))))) (PU ???))) ",
               C("det(??????-4, ??????-1)",
                 "acl(??????-4, ??????-2)",
                 "mark(??????-2, ???-3)",
                 "nsubj(??????-5, ??????-4)",
                 "root(ROOT-0, ??????-5)",
                 "amod(??????-8, ??????-6)",
                 "mark(??????-6, ???-7)",
                 "dobj(??????-5, ??????-8)",
                 "punct(??????-5, ???-9)",
                 "nsubj(???-11, ??????-10)",
                 "conj(??????-5, ???-11)",
                 "aux:asp(???-11, ???-12)",
                 "dobj(???-11, ??????-13)",
                 "punct(???-11, ???-14)",
                 "conj(???-11, ???-15)",
                 "aux:asp(???-15, ???-16)",
                 "amod(???-18, ???-17)",
                 "dobj(???-15, ???-18)",
                 "punct(???-11, ???-19)",
                 "conj(???-11, ??????-20)",
                 "nsubj(??????-23, ???????????????-21)",
                 "advmod(??????-23, ??????-22)",
                 "ccomp(??????-20, ??????-23)",
                 "discourse(??????-23, ???-24)",
                 "punct(??????-5, ???-25)")),

       T("( (IP (NP (NR ??????)) (VP (PP (P ???) (LCP (IP (VP (VV ??????))) (LC ??????))) (ADVP (AD ???)) (ADVP (AD ??????)) (VP (VV ??????) (NP (NP (NR ????????????)) (CP (CP (IP (VP (PP (P ??????) (NP (NN ??????) (NN ??????))) (VP (MSP ???) (VP (VV ??????) )))) (DEC ???))) (NP (NN ??????) (NN ??????))))) (PU ???))) ",
               C("nsubj(??????-7, ??????-1)",
                 "case(??????-3, ???-2)",
                 "nmod:prep(??????-7, ??????-3)",
                 "case(??????-3, ??????-4)",
                 "advmod(??????-7, ???-5)",
                 "advmod(??????-7, ??????-6)",
                 "root(ROOT-0, ??????-7)",
                 "nmod(??????-16, ????????????-8)",
                 "case(??????-11, ??????-9)",
                 "compound:nn(??????-11, ??????-10)",
                 "nmod:prep(??????-13, ??????-11)",
                 "aux:prtmod(??????-13, ???-12)",
                 "acl(??????-16, ??????-13)",
                 "mark(??????-13, ???-14)",
                 "compound:nn(??????-16, ??????-15)",
                 "dobj(??????-7, ??????-16)",
                 "punct(??????-7, ???-17)")),

      // TODO(pliang): add more test cases for all the relations not covered (see WARNING below)
    });

    Set<String> ignoreRelations = new HashSet<>(Arrays.asList("subj", "obj", "mod"));
    // Make sure all the relations are tested for
    Set<String> testedRelations = new HashSet<String>();
    for (Pair<String, String> ex : examples) {
      for (String item : ex.second.split("\n"))
        testedRelations.add(item.substring(0, item.indexOf('(')));
    }
    for (String relation : UniversalChineseGrammaticalRelations.shortNameToGRel.keySet()) {
      if (!testedRelations.contains(relation))
        if ( ! ignoreRelations.contains(relation)) {
          System.err.println("WARNING: relation '" + relation + "' not tested");
        }
    }

    TreeReaderFactory trf = new PennTreeReaderFactory();
    for (Pair<String, String> ex : examples) {
      String testTree = ex.first;
      String testAnswer = ex.second;

      // specifying our own TreeReaderFactory is vital so that functional
      // categories - that is -TMP and -ADV in particular - are not stripped off
      Tree tree = Tree.valueOf(testTree, trf);
      GrammaticalStructure gs = new UniversalChineseGrammaticalStructure(tree, Filters.acceptFilter()); // include punct

      assertEquals("Unexpected CC processed dependencies for tree "+testTree,
          testAnswer,
          GrammaticalStructureConversionUtils.dependenciesToString(gs, gs.typedDependenciesCCprocessed(GrammaticalStructure.Extras.MAXIMAL), tree, false, false, false));
    }
  }

}
