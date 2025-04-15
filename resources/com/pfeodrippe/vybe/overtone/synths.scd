Server.default.reboot;

UGen.allSubclasses[0].ar.inputs;
Line.ar.argNameForInputAt(0);
FoaDecode.ar.argNames; // THIS DOESN'T WORK!!!

(
~enc = FoaEncoderMatrix.newDirection(theta: 0, phi: 0);
~dec = FoaDecoderMatrix.newStereo(angle: 110.degrad/2, pattern: 0.5);

b = Buffer.read(s, "/Users/pfeodrippe/Library/Application Support/ATK/sounds/stereo/Aurora_Surgit-Dies_Irae.wav");
)

(
SynthDef(\ambi, {
	var sig, azim;
	azim = LFSaw.kr(1/12).bipolar(180).poll;
	//sig = SinOsc.ar(550, 0, 0.2);
	sig = PlayBuf.ar(1, b, BufRateScale.ir(b), loop: 1);
	sig = FoaEncode.ar(sig, ~enc);
	sig = FoaRotate.ar(sig, azim.degrad);
	sig = FoaDecode.ar(sig, ~dec);
	Out.ar(0, sig);
}).play;
)

(
	o = ServerOptions.new;
	o.maxLogins = 32;
	~t = Server.remote(\eita11, NetAddr("localhost", 3151), o);
)
~t.makeWindow
~t.scope
~t.freqscope
~t.plotTree
~t.plotTreeView
~t.serverBooting

/////////////////////////////////////////////////////////////// ----------------------------------
// EXAMPLES using synthdefs below

(
SynthDef(\simple, {|
	out_bus = 0
	|

	var sig, azim;
	//sig = SinOsc.ar(550, 0, 0.2);
	sig = PlayBuf.ar(1, b, BufRateScale.ir(b), loop: 1);
	Out.ar(out_bus, sig);
}).add;
)

~myBus = Bus.audio(s, 1);
(
~aa = Synth(\simple, [\out_bus, ~myBus]);
~ss = Synth(\directional, [\in, ~myBus], ~aa, \addAfter);
)
(~ss.set(\azim, 180);)
~ss.free;

/////////////////////////////////////////////////////////////// ----------------------------------
// Synthdefs
s.reboot;

(
~resourcesPath = "/Users/pfeodrippe/dev/vybe/resources/com/pfeodrippe/vybe/overtone";
~writeDefFile = { arg synth;
	var dirFile = File(~resourcesPath ++ "/" ++ synth.name ++ ".scsyndef", "wb");
	synth.asArray.writeDef(dirFile);
	dirFile.close;
}
)

(
var directional = SynthDef('directional', {|
	in = 10,
	out_bus = 0
    |

	var sig, azim, enc, dec;
	enc = FoaEncoderMatrix.newDirection(theta: 0, phi: 0);
	dec = FoaDecoderMatrix.newStereo(angle: 110.degrad/2, pattern: 0.5);

	//azim = LFSaw.kr(1/12).bipolar(180).poll;
	sig = In.ar(in, 1);
	sig = FoaEncode.ar(sig, enc);
	sig = FoaRotate.ar(sig, \azim.kr(0));
	sig = FoaTilt.ar(sig, \elev.kr(0));
	//sig = FoaProximity.ar(sig, \distance.kr(1) + 0.01);
	sig = FoaDecode.ar(sig, dec);
	Out.ar(out_bus, sig * \amp.kr(1));
});
~writeDefFile.value(directional);
//.writeDefFile(~resourcesPath).add;

var bassdrum = SynthDef('sonic-pi-sc808_bassdrum', {|
    note = 34,
    amp = 1, amp_slide = 0, amp_slide_shape = 1, amp_slide_curve = 0,
    pan = 0, pan_slide = 0, pan_slide_shape = 1, pan_slide_curve = 0,
    click = 0.11
    decay = 2,
    decay_curve = -7
    attenuation=1,
    out_bus = 0|

    var snd, fenv, env, trienv, sig, sub, punch, pfenv;

    note = note.midicps;
    amp = 2 * amp;
    amp = amp.varlag(amp_slide, amp_slide_curve, amp_slide_shape);
    pan = pan.varlag(pan_slide, pan_slide_curve, pan_slide_shape);

    env = EnvGen.kr(Env.new([click, 1, 0], [0, decay], decay_curve),doneAction:2);
    trienv = EnvGen.kr(Env.new([click, 0.6, 0], [0, decay], -230),doneAction:0);
    fenv = Env([note*7, note*1.35, note], [0.05/attenuation, 0.6], -14).kr; // Mod 0.05 -> 0.5
    pfenv = Env([note*7, note*1.35, note], [0.03/attenuation, 0.6], -10).kr; // Mod 0.03 -> 0.3
    sig = SinOsc.ar(fenv, pi/2) * env;
    sub = LFTri.ar(fenv, pi/2) * trienv * 0.05;
    punch = SinOsc.ar(pfenv, pi/2) * env * 2;
    punch = HPF.ar(punch, 350);
    sig = (sig + sub + punch) * 2.5;
    sig = Limiter.ar(sig, 0.5) * amp;
    DetectSilence.ar(sig, doneAction: Done.freeSelf);
    sig = Pan2.ar(sig, pan);
    Out.ar(out_bus, sig);
});
//~writeDefFile.value(bassdrum);
)