#version 330

in vec2 fragTexCoord;
in vec4 fragColor;

// Input uniform values
uniform sampler2D texture0;
uniform sampler2D texture1;
uniform vec4 colDiffuse;
uniform float u_fill;
uniform float u_time;

// Output fragment color
out vec4 finalColor;

bool fill_whole(vec2 st) {
    float c = 1.0;
    bool trigger =
        st.x * st.y < sin(u_fill * 0.87 * c) * cos(u_fill * 0.23 * c) + sin(u_fill * 0.37 * c) * cos(u_fill * 0.53 * c)
        //st.x > sin(u_fill * 0.24) * 1.0 && st.x < sin(u_fill * 0.87 * c) &&
        //st.y > sin(u_fill * 0.41 * c) * 0.4 && st.y < sin(u_fill * 0.46 * c)
        ;

    return trigger;
}

bool fill_blob(vec2 st) {
    if (u_fill < -0.5) return false;

    float c = 0.5;
    bool trigger = false;

    float t = sin(u_time * 0.8) * 0.1;
    float r = u_fill * 0.5 + t;


    // Circle.
    if ((st.x - c) * (st.x - c) + (st.y - c) * (st.y - c) < r/5.0) {
        trigger = true;
    }

    // Square.
    // if (st.x - 0.5 < 0.25 * u_fill && st.y - 0.5 < 0.25 * u_fill &&
    //     st.x - 0.5 > -0.25 * u_fill && st.y - 0.5 > -0.25 * u_fill) {
    //     trigger = true;
    // }

    /*
    if ((sin(st.x + cos(u_time*0.532) * 0.05) - c) * (sin(st.x + cos(u_time*0.3) * 0.04) - c) +
        (sin(st.y + sin(u_time * 0.2) * 0.145) - c) * (sin(st.y + sin(u_time * 0.153) * 0.1) - c)
        < r/1.0 * st.x * st.y * cos(st.x * u_time * 0.1) * sin(st.y * u_time * 0.2)) {
        trigger = true;
    }
    */

    /*
    if ((sin(st.x + cos(u_time*0.532) * 0.05) - c) * (sin(st.x + cos(u_time*0.3) * 0.04) - c) +
        (sin(st.y + sin(u_time * 0.2) * 0.145) - c) * (sin(st.y + sin(u_time * 0.153) * 0.1) - c)
        < r/8.0) {
        trigger = true;
    }
    */

    // dots
    /*
    if ((st.x - c) * (st.x - c) + (st.y - c) * (st.y - c) < r/5.0) {
        if (sin(st.y*99999.0) > 0.0001 && cos(st.x*100000.0) > 0.0001) {
            trigger = true;
        } else {
            trigger = false;
        }
    }
    */

    return trigger;
}

void main (void) {
    vec2 st = fragTexCoord;

    float c = 1.0;
    bool trigger =
        //fill_whole(st)
        fill_blob(st)
        ;

    if (trigger) {
        // Texel color fetching from texture sampler
        vec4 texelColor = texture(texture1, fragTexCoord);

        finalColor = texelColor*colDiffuse*fragColor;
    } else {
        // Texel color fetching from texture sampler
        vec4 texelColor = texture(texture0, fragTexCoord);

        finalColor = texelColor*colDiffuse*fragColor;
    }
}
