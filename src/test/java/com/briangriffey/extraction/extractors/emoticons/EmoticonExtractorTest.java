package com.briangriffey.extraction.extractors.emoticons;

import com.briangriffey.extraction.extractors.emoticons.EmoticonExtraction;
import com.briangriffey.extraction.extractors.emoticons.EmoticonExtractor;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class EmoticonExtractorTest {

    private EmoticonExtractor extractor;

    @Before
    public void setup() {
        this.extractor = new EmoticonExtractor();
    }

    @Test
    public void testNull() {
        //sending in null should yield 0 results
        Iterator<EmoticonExtraction> iterator = extractor.getExtractedFeatures(null).toBlocking().getIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAllTheEmoticons() {
        int i = 0;
        for (EmoticonExtraction extraction : extractor.getExtractedFeatures(ALL_THE_EMOTICONS).toBlocking().toIterable()) {
            i++;
        }

        //The direction explicitly say to not parse anything over 15 characters so challengeaccepted does not match the emoticons list
        assertEquals(204, i);
    }

    @Test
    public void testHuggingEmoticons() {
        //my aunt always uses ((( as hugs when she texts, so let's try that
        Iterator<EmoticonExtraction> iterator = extractor.getExtractedFeatures("Brian I love to give you (((hugs))), over coffee at noon").toBlocking().getIterator();

        EmoticonExtraction extraction = iterator.next();
        assertNotNull(extraction);

        assertEquals(33, extraction.getEnd());
        assertEquals(27, extraction.getStart());
        assertEquals("(hugs)", extraction.getExtraction());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyBody() {
        Iterator<EmoticonExtraction> iterator = extractor.getExtractedFeatures("()").toBlocking().getIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testBadChars() {
        Iterator<EmoticonExtraction> iterator = extractor.getExtractedFeatures("This shouldn't trigger na (emotias,.)").toBlocking().getIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAlphaNumeric() {
        Iterator<EmoticonExtraction> iterator = extractor.getExtractedFeatures("This shouldn't(ba298) stuff)").toBlocking().getIterator();
        EmoticonExtraction extraction = iterator.next();
        assertNotNull(extraction);
        assertEquals(14, extraction.getStart());
        assertEquals(21, extraction.getEnd());
        assertEquals("This shouldn't(ba298) stuff)", extraction.getSource());
        assertEquals("(ba298)", extraction.getExtraction());
    }

    @Test
    public void testCharRange() {
        int i= 0;
        for (EmoticonExtraction extraction : extractor.getExtractedFeatures("(1)(12)(123)(1234)(12345)(123456)(123467)(12345678)(123456789)(1234567890)(12345678901)(123456789012)(1234567890123)(12345678901234)(123456789012345)(123456789012345678)").toBlocking().toIterable()) {
            i++;
        }

        assertEquals(15, i);
    }

    private static final String ALL_THE_EMOTICONS =
            "(allthethings)" +
                    "(android)" +
                    "(areyoukiddingme)" +
                    "(arrington)" +
                    "(arya)" +
                    "(ashton)" +
                    "(atlassian)" +
                    "(awesome)" +
                    "(awthanks)\n" +
                    "(aww)\n" +
                    "(awwyiss)\n" +
                    "(badjokeeel)\n" +
                    "(badpokerface)\n" +
                    "(badtime)\n" +
                    "(bamboo)\n" +
                    "(ban)\n" +
                    "(banks)\n" +
                    "(basket)\n" +
                    "(beer)\n" +
                    "(bicepleft)\n" +
                    "(bicepright)\n" +
                    "(bitbucket)\n" +
                    "(boom)\n" +
                    "(borat)\n" +
                    "(branch)\n" +
                    "(bumble)\n" +
                    "(bunny)\n" +
                    "(cadbury)\n" +
                    "(cake)\n" +
                    "(candycorn)\n" +
                    "(carl)\n" +
                    "(caruso)\n" +
                    "(catchemall)\n" +
                    "(ceilingcat)\n" +
                    "(celeryman)\n" +
                    "(cereal)\n" +
                    "(cerealspit)\n" +
                    "(challengeaccepted)\n" +
                    "(chef)\n" +
                    "(chewie)\n" +
                    "(chocobunny)\n" +
                    "(chompy)\n" +
                    "(chucknorris)\n" +
                    "(clarence)\n" +
                    "(coffee)\n" +
                    "(confluence)\n" +
                    "(content)\n" +
                    "(continue)\n" +
                    "(cookie)\n" +
                    "(cornelius)\n" +
                    "(corpsethumb)\n" +
                    "(crucible)\n" +
                    "(daenerys)\n" +
                    "(dance)\n" +
                    "(dealwithit)\n" +
                    "(derp)\n" +
                    "(disappear)\n" +
                    "(disapproval)\n" +
                    "(doge)\n" +
                    "(doh)\n" +
                    "(donotwant)\n" +
                    "(dosequis)\n" +
                    "(downvote)\n" +
                    "(drevil)\n" +
                    "(drool)\n" +
                    "(ducreux)\n" +
                    "(dumb)\n" +
                    "(dwaboutit)\n" +
                    "(evilburns)\n" +
                    "(excellent)\n" +
                    "(facepalm)\n" +
                    "(failed)\n" +
                    "(feelsbadman)\n" +
                    "(feelsgoodman)\n" +
                    "(finn)\n" +
                    "(fireworks)\n" +
                    "(fisheye)\n" +
                    "(fonzie)\n" +
                    "(foreveralone)\n" +
                    "(forscale)\n" +
                    "(freddie)\n" +
                    "(fry)\n" +
                    "(ftfy)\n" +
                    "(fu)\n" +
                    "(fuckyeah)\n" +
                    "(fwp)\n" +
                    "(gangnamstyle)\n" +
                    "(gates)\n" +
                    "(ghost)\n" +
                    "(giggity)\n" +
                    "(goldstar)\n" +
                    "(goodnews)\n" +
                    "(greenbeer)\n" +
                    "(grumpycat)\n" +
                    "(gtfo)\n" +
                    "(haha)\n" +
                    "(haveaseat)\n" +
                    "(heart)\n" +
                    "(heygirl)\n" +
                    "(hipchat)\n" +
                    "(hipster)\n" +
                    "(hodor)\n" +
                    "(huehue)\n" +
                    "(hugefan)\n" +
                    "(huh)\n" +
                    "(ilied)\n" +
                    "(indeed)\n" +
                    "(iseewhatyoudidthere)\n" +
                    "(itsatrap)\n" +
                    "(jackie)\n" +
                    "(jaime)\n" +
                    "(jake)\n" +
                    "(jira)\n" +
                    "(jobs)\n" +
                    "(joffrey)\n" +
                    "(jonsnow)\n" +
                    "(kennypowers)\n" +
                    "(krang)\n" +
                    "(kwanzaa)\n" +
                    "(lincoln)\n" +
                    "(lol)\n" +
                    "(lolwut)\n" +
                    "(megusta)\n" +
                    "(meh)\n" +
                    "(menorah)\n" +
                    "(mindblown)\n" +
                    "(motherofgod)\n" +
                    "(ned)\n" +
                    "(nextgendev)\n" +
                    "(nice)\n" +
                    "(ninja)\n" +
                    "(noidea)\n" +
                    "(nothingtodohere)\n" +
                    "(notit)\n" +
                    "(notsureif)\n" +
                    "(notsureifgusta)\n" +
                    "(ohcrap)\n" +
                    "(ohgodwhy)\n" +
                    "(ohmy)\n" +
                    "(okay)\n" +
                    "(omg)\n" +
                    "(orly)\n" +
                    "(paddlin)\n" +
                    "(pbr)\n" +
                    "(philosoraptor)\n" +
                    "(pingpong)\n" +
                    "(pirate)\n" +
                    "(pokerface)\n" +
                    "(poo)\n" +
                    "(present)\n" +
                    "(pride)\n" +
                    "(pumpkin)\n" +
                    "(rageguy)\n" +
                    "(rainicorn)\n" +
                    "(rebeccablack)\n" +
                    "(reddit)\n" +
                    "(rockon)\n" +
                    "(romney)\n" +
                    "(rudolph)\n" +
                    "(sadpanda)\n" +
                    "(sadtroll)\n" +
                    "(salute)\n" +
                    "(samuel)\n" +
                    "(santa)\n" +
                    "(sap)\n" +
                    "(scumbag)\n" +
                    "(seomoz)\n" +
                    "(shamrock)\n" +
                    "(shrug)\n" +
                    "(skyrim)\n" +
                    "(sourcetree)\n" +
                    "(stare)\n" +
                    "(stash)\n" +
                    "(success)\n" +
                    "(successful)\n" +
                    "(sweetjesus)\n" +
                    "(tableflip)\n" +
                    "(taco)\n" +
                    "(taft)\n" +
                    "(tea)\n" +
                    "(thatthing)\n" +
                    "(theyregreat)\n" +
                    "(tree)\n" +
                    "(troll)\n" +
                    "(truestory)\n" +
                    "(trump)\n" +
                    "(turkey)\n" +
                    "(twss)\n" +
                    "(tyrion)\n" +
                    "(tywin)\n" +
                    "(unacceptable)\n" +
                    "(unknown)\n" +
                    "(upvote)\n" +
                    "(vote)\n" +
                    "(waiting)\n" +
                    "(washington)\n" +
                    "(wat)\n" +
                    "(whoa)\n" +
                    "(whynotboth)\n" +
                    "(wtf)\n" +
                    "(yey)\n" +
                    "(yodawg)\n" +
                    "(youdontsay)\n" +
                    "(yougotitdude)\n" +
                    "(yuno)\n" +
                    "(zoidberg)";
}
